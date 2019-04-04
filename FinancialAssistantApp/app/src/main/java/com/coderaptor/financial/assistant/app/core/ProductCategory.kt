package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class ProductCategory(val name: String, val productPropertyId: Long, val productPropertyValue: String) {
    var id: Long = -1

    constructor(id: Long, name: String, productPropertyId: Long, productPropertyValue: String): this(name, productPropertyId, productPropertyValue) {
        this.id = id
    }


    companion object {
        val CREATE_TABLE_PRODUCT_CATEGORY = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_PRODUCT_CATEGORY} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_NAME} TEXT, " +
                "${DatabaseHandler.PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY} INTEGER, " +
                "${DatabaseHandler.PRODUCT_PROPERTY_VALUE} TEXT, " +
                "FOREIGN KEY(${DatabaseHandler.PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY}) REFERENCES ${DatabaseHandler.TABLE_NAME_PRODUCT_PROPERTY}(${DatabaseHandler.BASE_ID})) "
    }

    override fun toString(): String {
        return "ProductCategory(id=$id, name='$name', productPropertyId=$productPropertyId, productPropertyValue='$productPropertyValue')"
    }
}