package com.coderaptor.financial.assistant.app.data

enum class ProductCategoryEnum(val categoryName: String, val propertyId: Int,  val propertyValue: String) {
    ELELMISZER("Élelmiszer", 1, "false"),
    RAGCSA("Rágcsa", 1, "true"),
    UDITO("Üditő", 1, "false"),
    ALKOHOL("Alkohol", 1, "true"),
    RUHA("Ruha", 1, "false"),
    ELEKTRONIKA("Elektronika", 2, "true"),
    SZERSZAM("Szerszám", 2, "false"),
    ESZKOZ("Eszköz", 2, "false")

}