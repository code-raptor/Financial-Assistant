package com.coderaptor.financial.assistant.app.data

enum class ProductPropertyEnum(val propertyName: String, val type: String) {
    KAROS("Káros", "Boolean"),
    JOTALLAS("Jotállás", "Boolean"),
    JOTALLAS_VEGE("Lejárat", "Date")
}