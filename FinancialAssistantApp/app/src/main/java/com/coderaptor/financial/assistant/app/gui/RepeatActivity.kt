package com.coderaptor.financial.assistant.app.gui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.MainActivity
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.adapters.TransactionViewHolder
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.fieldsEmpty
import com.coderaptor.financial.assistant.app.util.formatDate
import com.coderaptor.financial.assistant.app.util.openCalendar
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_repeats.*
import kotlinx.android.synthetic.main.dialog_add_repeat.*
import kotlinx.android.synthetic.main.dialog_add_repeat.view.*

class RepeatActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeats)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val list = dbHandler.findAllTransaction("${DatabaseHandler.FREQUENCY_TRANSACTION} != 'Egyszeri'")
        val dataSource: DataSource<Any> = dataSourceOf(list)

        savefab.setOnClickListener{
            MaterialDialog(this).show {
                setTheme(R.style.AppTheme)
                title(R.string.newRepeatTransaction)
                customView(R.layout.dialog_add_repeat, scrollable = true)

                val datefield = getCustomView().dateField
                datefield.isClickable = true
                datefield.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
                datefield.setOnClickListener {
                    openCalendar(it.dateField)
                }

                positiveButton(R.string.save) { dialog ->
                    val result = fieldsEmpty(amountField.text)

                    if(result) {
                        var amount = dialog.getCustomView().amountField.text.toString().toInt()
                        if (kiadas.isChecked) amount = amountField.text.toString().toInt() * -1
                        val date = dialog.getCustomView().dateField.text.toString()
                        val category = dialog.getCustomView().categoryField.selectedItem.toString()
                        val frequency = dialog.getCustomView().frequencyField.selectedItem.toString()
                        val comment = descriptField.text.toString()
                        var transaction = Transaction(amount, date, category, frequency)
                        if (comment.isNotEmpty()) {
                            transaction = Transaction(amount, date, category, comment, frequency)
                        }

                        dbHandler.insert(transaction)
                        dataSource.add(transaction)
                        toast("Sikeres hozzáadás!")
                        if (dbHandler.getCurrentLimit() < 0) {
                            toast("Napi limit meghaladva!")
                        }
                    }
                    else{
                        toast("Hiányzó adat!")
                    }
                }
                negativeButton(R.string.cancel)
            }

        }

        recyclerView.setup {

            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    toast("delete $index: ${item}")
                    if (item is Transaction) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
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
        }
    }
}

