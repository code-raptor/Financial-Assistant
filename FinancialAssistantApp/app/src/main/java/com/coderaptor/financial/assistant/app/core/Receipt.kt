package com.coderaptor.financial.assistant.app.core

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
        const val CREATE_TABLE_RECEIPT = ""
    }
}