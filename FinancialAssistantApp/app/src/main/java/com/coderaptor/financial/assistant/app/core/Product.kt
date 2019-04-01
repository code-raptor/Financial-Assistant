package com.coderaptor.financial.assistant.app.core

data class Product(val name: String, val unit: String, val quantity: Int, val unitPrice: Int, val categoryId: Long) {
    var id: Long = -1

    constructor(id: Long, name: String, unit: String, quantity: Int, unitPrice: Int, categoryId: Long): this(name, unit, quantity, unitPrice, categoryId) {
        this.id = id
    }

    companion object {
        val CREATE_TABLE_PRODUCT = ""
    }
}