package com.coderaptor.financial.assistant.app.features.oneweek

import android.util.Log
import com.coderaptor.financial.assistant.app.data.DatabaseHandler

fun getOneWeekData(dbHandler: DatabaseHandler): MutableList<Any> {
    val selection = "date(${DatabaseHandler.BASE_DATE}) BETWEEN date('now', '-7 day') AND date('now')"
    val transactions = dbHandler.findAllTransaction(selection)
    Log.i("oneWeek", "t list size: ${transactions.size}")

    val receipts = dbHandler.findAllReceipt(selection)
    Log.i("oneWeek", "r list size: ${receipts.size}")

    val list = mutableListOf<Any>()
    list.addAll(transactions)
    list.addAll(receipts)

    return list
}