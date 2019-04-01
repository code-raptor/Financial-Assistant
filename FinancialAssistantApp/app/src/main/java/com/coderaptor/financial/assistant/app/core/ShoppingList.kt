package com.coderaptor.financial.assistant.app.core

data class ShoppingList(val name: String, val quantity: Int) {
    var id: Long = -1
    var productId: Long = -1

    constructor(id: Long, name: String, quantity: Int): this(name, quantity) {
        this.id = id
    }

    constructor(id: Long, name: String, quantity: Int, productId: Long): this(name, quantity) {
        this.id = id
        this.productId = productId
    }

    companion object {
        val CREATE_TABLE_SHOPPING_LIST = ""
    }
}