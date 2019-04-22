package com.coderaptor.financial.assistant.app.util

import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.widget.Toast

fun java.util.Calendar.formatDate(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(this.time)
}

private var toast: Toast? = null

internal fun Activity.toast(message: CharSequence) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .apply { show() }
}
