package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Transaction(val amount: Int, val date: String, val name: String, val comment: String = "", val frequency: String = "Egyszeri") {
    var id: Long = -1

    constructor(id: Long = -1, amount: Int, date: String, name: String, comment: String = "", frequency: String) :
            this(amount, date, name, comment, frequency) {
        this.id = id
    }

    fun hasFrequency() = (frequency.isNotEmpty() && frequency != "Egyszeri")

    companion object {
        const val CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_TRANSACTION} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_AMOUNT} INTEGER, " +
                "${DatabaseHandler.BASE_DATE} TEXT, " +
                "${DatabaseHandler.BASE_NAME} TEXT, " +
                "${DatabaseHandler.BASE_COMMENT} TEXT, " +
                "${DatabaseHandler.FREQUENCY_TRANSACTION} TEXT)"
    }
}