package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Receipt(val date: String, val amount: Int, val productId: Long) {
    var id: Long = -1
    var productIds = ArrayList<Long>()

    constructor(id: Long, date: String, amount: Int, productId: Long): this(date, amount, productId) {
        this.id = id
    }

    constructor(id: Long, date: String, amount: Int, productIds: ArrayList<Long>): this(date, amount, productIds[0]) {
        this.id = id
        this.productIds = productIds
    }

    companion object {
        val CREATE_TABLE_RECEIPT = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_RECEIPT} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY," +
                "${DatabaseHandler.BASE_DATE} TEXT, " +
                "${DatabaseHandler.BASE_AMOUNT} INTEGER, " +
                "${DatabaseHandler.PRODUCT_ID_RECEIPT} INTEGER, " +
                "FOREIGN KEY(${DatabaseHandler.PRODUCT_ID_RECEIPT}) REFERENCES ${DatabaseHandler.TABLE_NAME_PRODUCT}(${DatabaseHandler.BASE_ID}), " +
                "UNIQUE (${DatabaseHandler.BASE_ID}, ${DatabaseHandler.BASE_DATE}, ${DatabaseHandler.BASE_AMOUNT}))"

    }

    override fun toString(): String {
        return "Receipt(id=$id, date='$date', amount=$amount, productId=$productId)"
    }
}