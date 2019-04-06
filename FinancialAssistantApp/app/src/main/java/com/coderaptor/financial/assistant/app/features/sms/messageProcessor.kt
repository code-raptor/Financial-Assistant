package com.coderaptor.financial.assistant.app.features.sms

fun findAmount(message: String): Int {
    val osszegStartIndex = message.lastIndexOf("összeg:")
    if(osszegStartIndex != -1) {
        val amountPlusCurrency = message.substring(osszegStartIndex + 8)
        val currencyStartIndex = amountPlusCurrency.lastIndexOf(" HUF")
        val amount = amountPlusCurrency.substring(0, currencyStartIndex)
        val dotDeletedAmount = amount.replace(".", "")
        return Integer.parseInt(dotDeletedAmount)
    }
    return -1
}