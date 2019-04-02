package com.coderaptor.financial.assistant.app.core

data class ProductCategory(val name: String, val productPropertyId: Long, val productPropertyValue: String) {
    var id: Long = -1

    constructor(id: Long, name: String, productPropertyId: Long, productPropertyValue: String): this(name, productPropertyId, productPropertyValue) {
        this.id = id
    }

    companion object {
        const val CREATE_TABLE_PRODUCT_CATEGORY = ""
    }
}