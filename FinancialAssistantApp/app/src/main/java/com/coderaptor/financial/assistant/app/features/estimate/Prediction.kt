package com.coderaptor.financial.assistant.app.features.estimate

import android.util.Log
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.core.ShoppingList
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import java.util.*

fun getProductToShoppingList(dbHandler: DatabaseHandler): ArrayList<ShoppingList> {
    val soppingList = ArrayList<ShoppingList>()
    val fiveWeekData = getweeksData(dbHandler, "-25 day", "now").groupBy { it.name }.filterValues { it.size >= 3 }
    if (fiveWeekData.isNotEmpty()) {
        val shoppingElement = fiveWeekData.values.first()
        shoppingElement.forEach { Log.i("joslas", "x: $it") }
        soppingList.add(ShoppingList(shoppingElement.first().name, shoppingElement.first().quantity, shoppingElement.first().unit, isBought = false))
    }
    return soppingList
}

fun getweeksData(dbHandler: DatabaseHandler, start: String, end: String): MutableList<Product> {
    val selection = "date(${DatabaseHandler.RECEIPT_DATE}) BETWEEN date('now', '$start') AND date('$end')"
    return dbHandler.findAllProduct(selection)
}