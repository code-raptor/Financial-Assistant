package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Dream(val name: String, val amount: Int, val where: String) {
    var id: Long = -1

    constructor(id: Long = -1, name: String, amount: Int, where: String): this(name, amount, where) {
        this.id = id
    }

    companion object {
        const val CREATE_TABLE_DREAM = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_DREAM} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_NAME} TEXT, " +
                "${DatabaseHandler.BASE_AMOUNT} INTEGER, " +
                "${DatabaseHandler.WHERE_DREAM} TEXT);"
    }
}