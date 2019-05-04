package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Product(val name: String, val unit: String, val quantity: Int, val unitPrice: Int, var receiptDate: String = "", val date: String = "", val categoryId: Long) {
    var id: Long = -1

    constructor(id: Long, name: String, unit: String, quantity: Int, unitPrice: Int, receiptDate: String = "", date: String, categoryId: Long): this(name, unit, quantity, unitPrice, receiptDate, date, categoryId) {
        this.id = id
    }

    constructor(name: String, unit: String, quantity: Int, price: Int, endDate: String, categoryId: Long) : this(name, unit, quantity, price, "", endDate, categoryId)

    companion object {
        val CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_PRODUCT} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_NAME} TEXT, " +
                "${DatabaseHandler.UNIT} TEXT, " +
                "${DatabaseHandler.BASE_QUANTITY} INTEGER, " +
                "${DatabaseHandler.UNIT_PRICE} INTEGER, " +
                "${DatabaseHandler.PRODUCT_CATEGORY_ID_PRODUCT} INTEGER, " +
                "${DatabaseHandler.BASE_DATE} TEXT, " +
                "${DatabaseHandler.RECEIPT_DATE} TEXT, " +
                "FOREIGN KEY(${DatabaseHandler.PRODUCT_CATEGORY_ID_PRODUCT}) REFERENCES ${DatabaseHandler.TABLE_NAME_PRODUCT_CATEGORY}(${DatabaseHandler.BASE_ID}))"
    }

    override fun toString(): String {
        return "Product(id=$id, name='$name', unit='$unit', quantity=$quantity, unitPrice=$unitPrice, receipt_date=$receiptDate, date=$date, categoryId=$categoryId)"
    }
}