package com.coderaptor.financial.assistant.app.features.estimate

import android.util.Log
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.core.ShoppingList
import com.coderaptor.financial.assistant.app.data.DatabaseHandler

fun getProductToShoppingList(dbHandler: DatabaseHandler): ArrayList<ShoppingList> {
    val soppingList = ArrayList<ShoppingList>()
    val fiveWeekData = getweeksData(dbHandler, "-25 day", "now")
    Log.i("joslas", "------------------------------------------------------------------------------")
    fiveWeekData.forEach { Log.i("joslas", "full: $it") }
    Log.i("joslas", "------------------------------------------------------------------------------")
    val resultMap = fiveWeekData.groupBy { it.name }
    resultMap.forEach { Log.i("joslas", "groupby1: $it") }
    Log.i("joslas", "------------------------------------------------------------------------------")
    val result = resultMap.filterValues { it.size >= 3 }
    if (result.isNotEmpty()) {
        val x = result.values.first()
        x.forEach { Log.i("joslas", "x: $it") }
        soppingList.add(ShoppingList(x.first().name, x.first().quantity, x.first().unit, isBought = false))
    }
    Log.i("joslas", "------------------------------------------------------------------------------")
    return soppingList
}

fun getweeksData(dbHandler: DatabaseHandler, start: String, end: String): MutableList<Product> {
    val selection = "date(${DatabaseHandler.RECEIPT_DATE}) BETWEEN date('now', '$start') AND date('$end')"
    return dbHandler.findAllProduct(selection)
}