package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class ShoppingList(val name: String, val quantity: Int, val unit: String, var isBought: Boolean = false) {
    var id: Long = -1
    var productId: Long = -1

    constructor(id: Long, name: String, quantity: Int, unit:String): this(name, quantity, unit) {
        this.id = id
    }

    constructor(id: Long, name: String, quantity: Int, unit: String, isBought: Boolean = false, productId: Long): this(name, quantity, unit, isBought) {
        this.id = id
        this.productId = productId
    }

    constructor(name: String, quantity: Int, unit: String, isBought: Boolean = false, productId: Long): this(name, quantity, unit, isBought) {
        this.productId = productId
    }


    companion object {
        const val CREATE_TABLE_SHOPPING_LIST= "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_SHOPPING} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_NAME} TEXT, " +
                "${DatabaseHandler.BASE_QUANTITY} INTEGER, " +
                "${DatabaseHandler.UNIT} TEXT, " +
                "${DatabaseHandler.BOUGHT} TEXT, " +
                "${DatabaseHandler.PRODUCT_ID_SHOPPING} INTEGER," +
                "FOREIGN KEY (${DatabaseHandler.PRODUCT_ID_SHOPPING}) REFERENCES ${DatabaseHandler.TABLE_NAME_PRODUCT}(${DatabaseHandler.BASE_ID}))"
    }

    override fun toString(): String {
        return "ShoppingList(id=$id, name='$name', quantity=$quantity, unit=$unit, bought=$isBought productId=$productId)"
    }
}