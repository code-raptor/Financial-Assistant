package com.coderaptor.financial.assistant.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
import com.coderaptor.financial.assistant.app.gui.dialogs.openOnceTransactionDialog
import com.coderaptor.financial.assistant.app.gui.dialogs.openRepeatDialog
import com.coderaptor.financial.assistant.app.util.SharedPreference
import com.coderaptor.financial.assistant.app.util.formatDate
import kotlinx.android.synthetic.main.activity_main.*
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
            dbHandler.insertTestdata()
            SharedPreference.firstRun = true
            SharedPreference.currentDate = Calendar.getInstance().formatDate()
        }else {
            Log.i("first", "Nem először value: ${SharedPreference.firstRun}")
            SharedPreference.currentDate = Calendar.getInstance().formatDate()
        }

        setupSms()
        checkDayChanged(dbHandler)

        val dataSource: DataSource<Any> = dataSourceOf(getOneWeekData(dbHandler))

        addNewButton.setOnClickListener {
            openOnceTransactionDialog(dataSource, dbHandler)
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

        recyclerView.setup {

            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    if (item is Transaction) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
                    } else if (item is Receipt) {
                        dbHandler.deleteByReceiptByReceiptId(item.baseID, DatabaseHandler.TABLE_NAME_RECEIPT)
                    }
                    true
                }
            }

            withSwipeAction(SwipeLocation.RIGHT) {
                icon(R.drawable.ic_edit_white_24dp)
                text(R.string.edit)
                color(R.color.edit)
                callback { index, item ->
                    if (item is Transaction) {
                        if (!item.hasFrequency()) {
                            Log.i("dialog", "hass: true")
                            openOnceTransactionDialog(dataSource, dbHandler, item)
                        }else {
                            openRepeatDialog(dataSource, dbHandler, item)
                        }
                    } else if (item is Receipt) {
                        val intent = Intent(this@MainActivity, ReceiptActivity::class.java)
                        intent.putExtra("id", item.baseID)
                        startActivity(intent)
                    }
                    false
                }
            }
            withEmptyView(nodata)
            withDataSource(dataSource)
            withItem<Transaction>(R.layout.list_income) {
                onBind(::TransactionViewHolder) { _, item ->
                    name.text = item.name
                    if(item.comment.isNotEmpty()) {
                        name.text = item.comment
                    }

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
                    //toast("Clicked $index: ${item.name}")
                }
            }
            withItem<Receipt>(R.layout.list_receipt) {
                onBind(::ReceiptViewHolder) { _, item ->
                    name.text = getString(R.string.receipt_string)
                    if(item.comment.isNotEmpty()) {
                        name.text = "NYUGTA: ${item.comment}"
                    }
                    date.text = item.date
                    amount.setTextColor(resources.getColor(R.color.amount_minus, null))
                    amount.text = "-${item.amount}"
                }
                onClick { index ->
                    //toast("Clicked $index: ${item}")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupSms() {
        val permissionGranted = askPermission(this, this)
        if (permissionGranted) {
            val hasNew = getSmsMessages(this)
            Log.i("sms", "hasNew: $hasNew")
            egyenleg.setText("~ ${SharedPreference.balance} ft")
        }else {
            SharedPreference.balance = 0
            egyenleg.setText(R.string.main_No_Amount)
        }
    }

    override fun onStop() {
        super.onStop()
        dbHandler.close()
    }
}