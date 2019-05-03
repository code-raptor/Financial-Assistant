package com.coderaptor.financial.assistant.app.util

import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.coderaptor.financial.assistant.app.R

fun java.util.Calendar.formatDate(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(this.time)
}

private var toast: Toast? = null

internal fun Activity.toast(message: CharSequence) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .apply { show() }
}

internal fun Activity.openCalendar(field: EditText) {
    MaterialDialog(this).show {
        setTheme(R.style.AppTheme)
        datePicker { _, innerDate ->
            field.text = Editable.Factory.getInstance().newEditable(innerDate.formatDate())
        }
    }
}

internal fun fieldsEmpty(vararg fields: Editable):Boolean{
    for (data in fields){
        if(data.isEmpty()){
            return false
        }
    }
    return true
}
