package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Receipt(val baseID: Long, val date: String, val amount: Int, val comment: String = "", val productId: Long) {
    var id: Long = -1

    constructor(id: Long, baseID: Long, date: String, amount: Int, comment: String = "", productId: Long): this(baseID, date, amount, comment, productId) {
        this.id = id
    }

    companion object {
        val CREATE_TABLE_RECEIPT = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_RECEIPT} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.RECEIPT_ID} INTEGER, " +
                "${DatabaseHandler.BASE_DATE} TEXT, " +
                "${DatabaseHandler.BASE_AMOUNT} INTEGER, " +
                "${DatabaseHandler.BASE_COMMENT} TEXT, " +
                "${DatabaseHandler.PRODUCT_ID_RECEIPT} INTEGER, " +
                "FOREIGN KEY(${DatabaseHandler.PRODUCT_ID_RECEIPT}) REFERENCES ${DatabaseHandler.TABLE_NAME_PRODUCT}(${DatabaseHandler.BASE_ID}), " +
                "UNIQUE (${DatabaseHandler.BASE_ID}, ${DatabaseHandler.BASE_DATE}, ${DatabaseHandler.BASE_AMOUNT}))"

    }

    override fun toString(): String {
        return "Receipt(id=$id, receipt_id=$baseID, date='$date', amount=$amount, comment=$comment, productId=$productId)"
    }
}