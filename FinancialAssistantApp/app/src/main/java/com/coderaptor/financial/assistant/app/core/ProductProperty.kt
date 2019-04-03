package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class ProductProperty(val name: String, val type: String) {
    var id: Long = -1

    constructor(id: Long = -1, name: String, type: String) :
            this(name, type) {
        this.id = id
    }

    companion object {
        const val CREATE_TABLE_PRODUCT_PROPERTY = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_PRODUCT_PROPERTY} " +
                "(${DatabaseHandler.PRODUCT_PROPERTY_ID} INTEGER PRIMARY KEY, ${DatabaseHandler.BASE_NAME} TEXT, ${DatabaseHandler.TYPE_PRODUCT_PROPERTY} TEXT)"
    }
}