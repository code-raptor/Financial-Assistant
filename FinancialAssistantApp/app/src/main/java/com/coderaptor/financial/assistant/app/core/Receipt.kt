package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Receipt(val date: String, val amount: Int, val product: Product) {
    var id: Long = -1
    var products = ArrayList<Product>()

    constructor(id: Long, date: String, amount: Int, product: Product): this(date, amount, product) {
        this.id = id
    }

    constructor(id: Long, date: String, amount: Int, products: ArrayList<Product>): this(date, amount, products[0]) {
        this.id = id
        this.products = products
    }

    companion object {
        const val CREATE_TABLE_RECEIPT = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_RECEIPT} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY," +
                "${DatabaseHandler.BASE_DATE} TEXT," +
                "${DatabaseHandler.BASE_AMOUNT} INTEGER"+
                "${DatabaseHandler.PRODUCT_ID_RECEIPT} INTEGER, FOREIGN KEY(${DatabaseHandler.PRODUCT_ID_RECEIPT}) REFERENCES ${DatabaseHandler.PRODUCT_ID})"

    }
}