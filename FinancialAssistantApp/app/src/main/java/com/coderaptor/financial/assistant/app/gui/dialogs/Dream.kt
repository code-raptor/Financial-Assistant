package com.coderaptor.financial.assistant.app.gui.dialogs

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.recyclical.datasource.DataSource
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.Dream
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.fieldsEmpty
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.dialog_add_dream.*
import kotlinx.android.synthetic.main.dialog_add_dream.view.*

fun Activity.openDreamDialog(dataSource: DataSource<Any>, dbHandler: DatabaseHandler, editData: Dream? = null) {
    MaterialDialog(this).show {
        setTheme(R.style.AppTheme)
        title(R.string.newDream)
        customView(R.layout.dialog_add_dream, scrollable = true)

        if (editData != null) {
            amountField.setText(editData.amount.toString())
            nameField.setText(editData.name)
            whereField.setText(editData.where)
        }

        positiveButton(R.string.save) { dialog ->
            val result = fieldsEmpty(nameField.text, amountField.text, whereField.text)

            if (result) {
                val amount = dialog.getCustomView().amountField.text.toString().toInt()
                val name = dialog.getCustomView().nameField.text.toString()
                val where = dialog.getCustomView().whereField.text.toString()
                val dream = Dream(name, amount, where)
                if (editData == null) {
                    dbHandler.insert(dream)
                    dataSource.add(dream)
                }else {
                    dataSource.remove(editData)
                    dbHandler.updateDream(dream, editData.id)
                    dataSource.add(dream)
                }
                toast("Sikeres hozzáadás!")
            } else {
                toast("Hiányzó adat!")
            }
        }
        negativeButton(R.string.cancel)
    }
}

