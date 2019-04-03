package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Product(val name: String, val unit: String, val quantity: Int, val unitPrice: Int, val categoryId: Long) {
    var id: Long = -1

    constructor(id: Long, name: String, unit: String, quantity: Int, unitPrice: Int, categoryId: Long): this(name, unit, quantity, unitPrice, categoryId) {
        this.id = id
    }

    companion object {
        const val CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_PRODUCT} " +
                "(${DatabaseHandler.PRODUCT_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_NAME} TEXT, " +
                "${DatabaseHandler.UNIT} TEXT, " +
                "${DatabaseHandler.BASE_QUANTITY} INTEGER)," +
                "${DatabaseHandler.UNIT_PRICE} INTEGER)," +
                "${DatabaseHandler.PRODUCT_CATEGORY_ID_PRODUCT} INTEGER, FOREIGN KEY(${DatabaseHandler.PRODUCT_CATEGORY_ID_PRODUCT}) REFERENCES ${DatabaseHandler.PRODUCT_CATEGORY_ID} )"
    }
}