package com.coderaptor.financial.assistant.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.adapters.ReceiptViewHolder
import com.coderaptor.financial.assistant.app.adapters.TransactionViewHolder
import com.coderaptor.financial.assistant.app.core.Receipt
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.features.limit.checkDayChanged
import com.coderaptor.financial.assistant.app.features.oneweek.getOneWeekData
import com.coderaptor.financial.assistant.app.features.sms.askPermission
import com.coderaptor.financial.assistant.app.features.sms.getSmsMessages
import com.coderaptor.financial.assistant.app.gui.DreamActivity
import com.coderaptor.financial.assistant.app.gui.RepeatActivity
import com.coderaptor.financial.assistant.app.util.SharedPreference
import com.coderaptor.financial.assistant.app.util.formatDate
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_income.*
import kotlinx.android.synthetic.main.dialog_add_income.view.*
import java.util.*


class MainActivity : AppCompatActivity(){

    val dbHandler = DatabaseHandler(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (!SharedPreference.firstRun) {
            SharedPreference.firstRun = true
            SharedPreference.currentDate = Calendar.getInstance().formatDate()
        }else {
            Log.i("first", "Nem először value: ${SharedPreference.firstRun}")
                SharedPreference.currentDate = Calendar.getInstance().formatDate()
        }

        setupSms(dbHandler.findMaxSMS())
        checkDayChanged(dbHandler)

        val list = getOneWeekData(dbHandler)

        dbHandler.insertTestdata()

        addNewButton.setOnClickListener {
            MaterialDialog(this).show {
                setTheme(R.style.AppTheme)
                title(R.string.onceNewtransaction)
                customView(R.layout.dialog_add_income, scrollable = true)

                val datefield = getCustomView().dateField
                datefield.isClickable = true
                datefield.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
                datefield.setOnClickListener {
                    dateClick(datefield)
                }

                positiveButton(R.string.save) { dialog ->
                    val result = fieldsEmpty(amountField.text)

                    if (result){

                        var amount: Int = amountField.text.toString().toInt()
                        if (kiadas.isChecked) amount = amountField.text.toString().toInt() * -1
                        val date = dateField.text.toString()
                        val category: String = categoryField.selectedItem.toString()

                        val transaction = Transaction(amount, date, category)

                        dbHandler.insert(transaction)
                        list.add(transaction)
                        if (dbHandler.getCurrentLimit() < 0) {
                            toast("Napi limit összeg meghaladva")
                        }

                        toast("sikeres hozzáadás")
                        }
                        else{
                            toast("Hiányzó adat!")
                        }
                    }
                negativeButton(R.string.cancel)
            }
        }

        repeatButton.setOnClickListener {
            val intent = Intent(this, RepeatActivity::class.java)
            startActivity(intent)
        }

        dreamButton.setOnClickListener {
            val intent = Intent(this, DreamActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        history.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        receiptButton.setOnClickListener {
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }

        shoppingListButton.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)
        }

        val dataSource: DataSource<Any> = dataSourceOf(list)

        recyclerView.setup {

            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    toast("delete $index: ${item}")
                    if (item is Transaction) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
                    } else if (item is Receipt) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_RECEIPT)
                    }
                    true
                }
            }

            withSwipeAction(SwipeLocation.RIGHT) {
                icon(R.drawable.ic_edit_white_24dp)
                text(R.string.edit)
                color(R.color.edit)
                callback { index, item ->
                    toast("edit $index: ${item}")
                    if (item is Transaction) {
                        //edit layout
                    } else if (item is Receipt) {
                        //edit layout
                    }
                    false
                }
            }
            withEmptyView(nodata)
            withDataSource(dataSource)
            withItem<Transaction>(R.layout.list_income) {
                onBind(::TransactionViewHolder) { _, item ->
                    name.text = item.name

                    if (item.hasFrequency()) {
                        date.text = item.date + getString(R.string.tab) + item.frequency
                    }else {
                        date.text = item.date
                    }

                    if (item.amount > 0) {
                        amount.setTextColor(resources.getColor(R.color.amount_plus, null))
                        amount.text = "+${item.amount}"
                    }else {
                        amount.setTextColor(resources.getColor(R.color.amount_minus, null))
                        amount.text = item.amount.toString()
                    }
                }
                onClick { index ->
                    toast("Clicked $index: ${item.name}")
                }
            }
            withItem<Receipt>(R.layout.list_receipt) {
                onBind(::ReceiptViewHolder) { _, item ->
                    name.text = getString(R.string.receipt_string)
                    date.text = item.date
                    amount.setTextColor(resources.getColor(R.color.amount_minus, null))
                    amount.text = "-${item.amount}"
                }
                onClick { index ->
                    toast("Clicked $index: ${item}")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupSms(findMaxSMS: Long) {
        val permissionGranted = askPermission(this, this)
        if(permissionGranted) {
            val idAndAmount = getSmsMessages(this, findMaxSMS, dbHandler)
            if (idAndAmount.first != (-1).toLong() && idAndAmount.second != -1) {
                dbHandler.insertSms(idAndAmount)
                egyenleg.setText("${dbHandler.getSmsAmount()} ft")
            }
        }
    }

    fun dateClick(field: EditText) {
        MaterialDialog(this).show {
            setTheme(R.style.AppTheme)
            datePicker { _, innerDate ->
                field.text = Editable.Factory.getInstance().newEditable(innerDate.formatDate())
            }
        }
    }

    fun fieldsEmpty(vararg fields: Editable):Boolean{
        for (data in fields){
            if(data.isEmpty()){
                return false
            }
        }
        return true
    }
}