package com.coderaptor.financial.assistant.app.gui.dialogs

import android.app.Activity
import android.text.Editable
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.recyclical.datasource.DataSource
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.fieldsEmpty
import com.coderaptor.financial.assistant.app.util.formatDate
import com.coderaptor.financial.assistant.app.util.openCalendar
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.dialog_add_repeat.*
import kotlinx.android.synthetic.main.dialog_add_repeat.view.*
import java.util.*

fun Activity.openRepeatDialog(dataSource: DataSource<Any>, dbHandler: DatabaseHandler, editData: Transaction? = null) {
    MaterialDialog(this).show {
        setTheme(R.style.AppTheme)
        title(R.string.newRepeatTransaction)
        customView(R.layout.dialog_add_repeat, scrollable = true)

        val datefield = getCustomView().dateField
        datefield.isClickable = true
        datefield.text = Editable.Factory.getInstance().newEditable(Calendar.getInstance().formatDate())
        datefield.setOnClickListener {
            openCalendar(it.dateField)
        }
        if (editData != null) {
            datefield.text = Editable.Factory.getInstance().newEditable(editData.date)
            kiadas.isChecked = (editData.amount < 0)
            bevetel.isChecked = !kiadas.isChecked
            if (kiadas.isChecked) {
                amountField.setText((editData.amount * -1).toString())
            }else {
                amountField.setText(editData.amount.toString())
            }
            descriptField.setText(editData.comment)
            var counter = -1
            val categoryList = resources.getStringArray(R.array.income_category).toList()
            for (item in categoryList) {
                counter++
                if (item == editData.name) {
                    break
                }
            }
            categoryField.setSelection(counter)

            counter = -1
            val frequencyList= resources.getStringArray(R.array.income_frequency).toList()
            for (item in frequencyList) {
                counter++
                if (item == editData.frequency) {
                    break
                }
            }
            frequencyField.setSelection(counter)
        }

        positiveButton(R.string.save) { dialog ->
            val result = fieldsEmpty(amountField.text)

            if (result) {
                var amount = dialog.getCustomView().amountField.text.toString().toInt()
                if (kiadas.isChecked) amount = amountField.text.toString().toInt() * -1
                val date = dialog.getCustomView().dateField.text.toString()
                val category = dialog.getCustomView().categoryField.selectedItem.toString()
                val frequency = dialog.getCustomView().frequencyField.selectedItem.toString()
                val comment = descriptField.text.toString()
                var transaction = Transaction(amount, date, category, frequency = frequency)
                if (comment.isNotEmpty()) {
                    transaction = Transaction(amount, date, category, comment, frequency)
                }
                if (editData == null) {
                    dbHandler.insert(transaction)
                    dataSource.add(transaction)
                    if (dbHandler.getCurrentLimit() < 0) {
                        toast("Napi limit meghaladva!")
                    }
                }else {
                    dataSource.remove(editData)
                    dbHandler.updateTransaction(transaction, editData.id)
                    dataSource.add(transaction)
                }
                //toast("Sikeres hozzáadás!")
            }else {
                toast("Hiányzó adat!")
            }
        }
        negativeButton(R.string.cancel)
    }
}