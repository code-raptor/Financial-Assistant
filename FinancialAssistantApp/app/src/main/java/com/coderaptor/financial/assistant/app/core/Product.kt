package com.coderaptor.financial.assistant.app.core

data class Product(val name: String, val productCategoryId: Long) {
    var id: Long = -1
    constructor(id: Long, name: String, productCategoryId: Long): this(name, productCategoryId) {
        this.id = id
    }

    companion object {
        val CREATE_TABLE_PRODUCT = ""
    }
}