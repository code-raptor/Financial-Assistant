package com.coderaptor.financial.assistant.app.core

data class Transaction(val amount: Int, val date: String, val category: String, val frequency: String) {
    var id: Long = -1

    constructor(id: Long = -1, amount: Int, date: String, category: String, frequency: String):
            this(amount, date ,category, frequency) {
                this.id = id
            }
}