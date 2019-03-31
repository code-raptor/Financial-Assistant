package com.coderaptor.financial.assistant.app.core

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

data class Transaction(val amount: Int, val date: String, val name: String, val frequency: String = "Egyszeri") {
    var id: Long = -1

    constructor(id: Long = -1, amount: Int, date: String, name: String, frequency: String) :
            this(amount, date, name, frequency) {
        this.id = id
    }

    fun hasFrequency() = (frequency.isNotEmpty() && frequency != "Egyszeri")

    companion object {
        val CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_TRANSACTION} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_AMOUNT} INTEGER, " +
                "${DatabaseHandler.DATE_TRANSACTION} TEXT, " +
                "${DatabaseHandler.BASE_NAME} TEXT, " +
                "${DatabaseHandler.FREQUENCY_TRANSACTION} TEXT)"
    }
}