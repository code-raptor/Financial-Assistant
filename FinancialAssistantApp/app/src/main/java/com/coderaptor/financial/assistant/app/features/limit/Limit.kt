package com.coderaptor.financial.assistant.app.features.limit

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.coderaptor.financial.assistant.app.data.DatabaseHandler

fun getCurrentDay(): Int {
    val calendar = Calendar.getInstance()
    val mdformat = SimpleDateFormat("dd")
    return mdformat.format(calendar.time).toInt()
}
fun setupLimit(limit: Int, dbHandler: DatabaseHandler) {
    if (dbHandler.limitsIsNotEquals(limit) || dbHandler.dayIsNotEquals(getCurrentDay())) {
        dbHandler.insertLimit(limit, getCurrentDay())
    }
}
fun checkDayChanged(dbHandler: DatabaseHandler) {
    if(dbHandler.dayIsNotEquals(getCurrentDay())) {
        dbHandler.insertDayToLimit(getCurrentDay())
    }
}