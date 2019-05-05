package com.coderaptor.financial.assistant.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.coderaptor.financial.assistant.app.core.*
import com.coderaptor.financial.assistant.app.core.Dream.Companion.CREATE_TABLE_DREAM
import com.coderaptor.financial.assistant.app.core.ProductProperty.Companion.CREATE_TABLE_PRODUCT_PROPERTY
import com.coderaptor.financial.assistant.app.core.Transaction.Companion.CREATE_TABLE_TRANSACTION
import com.coderaptor.financial.assistant.app.features.limit.getCurrentDay
import com.coderaptor.financial.assistant.app.util.SharedPreference

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        //TRANSACTION
        db.execSQL(CREATE_TABLE_TRANSACTION)
        Log.i("db", "Create Transaction table done!")
        //PRODUCT_PROPERTY
        db.execSQL(CREATE_TABLE_PRODUCT_PROPERTY)
        Log.i("db", "Create ProductProperty table done!")
        //DREAM
        db.execSQL(CREATE_TABLE_DREAM)
        Log.i("db", "Create Dream table done!")
        //PRODUCT_CATEGORY
        db.execSQL(ProductCategory.CREATE_TABLE_PRODUCT_CATEGORY)
        Log.i("db", "Create ProductCategory table done!")
        //PRODUCT
        db.execSQL(Product.CREATE_TABLE_PRODUCT)
        Log.i("db", "Create Product table done!")
        //SHOPPING
        db.execSQL(ShoppingList.CREATE_TABLE_SHOPPING_LIST)
        Log.i("db", "Create Shopping table done!")
        //RECEIPT
        db.execSQL(Receipt.CREATE_TABLE_RECEIPT)
        Log.i("db", "Create Receipt table done!")
        //Limit
        db.execSQL(CREATE_TABLE_LIMIT)
        Log.i("db", "Create LIMIT table done!")
        db.execSQL("INSERT INTO $TABLE_NAME_LIMIT($BASE_ID, $BASE_AMOUNT, $CURRENT_DAY_LIMIT) VALUES (1, $DEFAULT_LIMIT_AMOUNT, ${getCurrentDay()})")
        Log.i("db", "insert to $TABLE_NAME_LIMIT complete")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropTable(TABLE_NAME_TRANSACTION)
        dropTable(TABLE_NAME_PRODUCT_PROPERTY)
        dropTable(TABLE_NAME_DREAM)
        onCreate(db)
    }

    fun <T> insert(it: T): Long {
        val db = this.writableDatabase
        var id = (-1).toLong()
        when (it) {
            is Transaction -> {
                id = db.insert(TABLE_NAME_TRANSACTION, null, insertValuesTransaction(it))
                if (it.amount < 0) {
                    limitReduction(it.amount)
                }
                if (SharedPreference.firstRun) {
                    SharedPreference.balance += it.amount
                }
            }
            is ProductProperty -> {
                id = db.insert(TABLE_NAME_PRODUCT_PROPERTY, null, insertValuesProductProperty(it))
            }
            is Dream -> {
                id = db.insert(TABLE_NAME_DREAM, null, insertValuesDream(it))
            }
            is ProductCategory -> {
                id = db.insert(TABLE_NAME_PRODUCT_CATEGORY, null, insertValuesProductCategory(it))
            }
            is Product -> {
                id = db.insert(TABLE_NAME_PRODUCT, null, insertValuesProduct(it))
            }
            is ShoppingList -> {
                id = db.insert(TABLE_NAME_SHOPPING, null, insertValuesShoppingList(it))
            }
            is Receipt -> {
                id = db.insert(TABLE_NAME_RECEIPT, null, insertValuesReceipt(it))
                limitReduction(-it.amount)
                if (SharedPreference.firstRun) {
                    SharedPreference.balance += it.amount
                }
            }
        }
        db.close()
        return id
    }

    fun <T> inserts(myList: ArrayList<T>) {
        val db = this.writableDatabase
        myList.forEach {
            when (it) {
                is Transaction -> {
                    db.insert(TABLE_NAME_TRANSACTION, null, insertValuesTransaction(it))
                }
                is ProductProperty -> {
                    db.insert(TABLE_NAME_PRODUCT_PROPERTY, null, insertValuesProductProperty(it))
                }
                is Dream -> {
                    db.insert(TABLE_NAME_DREAM, null, insertValuesDream(it))
                }
                is ProductCategory -> {
                    db.insert(TABLE_NAME_PRODUCT_CATEGORY, null, insertValuesProductCategory(it))
                }
                is Product -> {
                    db.insert(TABLE_NAME_PRODUCT, null, insertValuesProduct(it))
                }
                is ShoppingList -> {
                    db.insert(TABLE_NAME_SHOPPING, null, insertValuesShoppingList(it))
                }
                is Receipt -> {
                    db.insert(TABLE_NAME_RECEIPT, null, insertValuesReceipt(it))
                }
            }
        }
        db.close()
    }

    fun findAllDream(): MutableList<Dream> {
        val list = mutableListOf<Dream>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_DREAM"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val amount = cursor.getInt(cursor.getColumnIndex(BASE_AMOUNT))
                val place = cursor.getString(cursor.getColumnIndex(WHERE_DREAM))

                val dream = Dream(id, name, amount, place)
                list.add(dream)
            }
        }
        cursor.close()
        db.close()
        return list
    }

    fun findAllTransaction(condition: String = ""): MutableList<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val db = readableDatabase
        var selectALLQuery = "SELECT * FROM $TABLE_NAME_TRANSACTION"
        if (condition.isNotEmpty()) {
            selectALLQuery = "SELECT * FROM $TABLE_NAME_TRANSACTION WHERE $condition"
        }
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val amount = cursor.getInt(cursor.getColumnIndex(BASE_AMOUNT))
                val date = cursor.getString(cursor.getColumnIndex(BASE_DATE))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val comment = cursor.getString(cursor.getColumnIndex(BASE_COMMENT))
                val frequency = cursor.getString(cursor.getColumnIndex(FREQUENCY_TRANSACTION))

                val transaction = Transaction(id, amount, date, name, comment, frequency)
                transactionList.add(transaction)
            }
        }
        cursor.close()
        db.close()
        return transactionList
    }

    private fun findAllProductProperty(): MutableList<ProductProperty> {
        val propertyList = mutableListOf<ProductProperty>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_PRODUCT_PROPERTY"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val type = cursor.getString(cursor.getColumnIndex(TYPE_PRODUCT_PROPERTY))

                val property = ProductProperty(id, name, type)
                propertyList.add(property)
            }
        }
        cursor.close()
        db.close()
        return propertyList
    }

    fun findAllProductCategory(): MutableList<ProductCategory> {
        val productCategoryList = mutableListOf<ProductCategory>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_PRODUCT_CATEGORY"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val productPropertyId = cursor.getLong(cursor.getColumnIndex(PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY))
                val productPropertyValue = cursor.getString(cursor.getColumnIndex(PRODUCT_PROPERTY_VALUE))

                val productCategory = ProductCategory(id, name, productPropertyId, productPropertyValue)
                productCategoryList.add(productCategory)
            }
        }
        cursor.close()
        db.close()
        return productCategoryList
    }

    fun findAllProduct(condition: String = ""): MutableList<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        var selectALLQuery = "SELECT * FROM $TABLE_NAME_PRODUCT"
        if (condition.isNotEmpty()) {
            selectALLQuery = "SELECT * FROM $TABLE_NAME_PRODUCT WHERE $condition"
        }
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val unit = cursor.getString(cursor.getColumnIndex(UNIT))
                val quantity = cursor.getInt(cursor.getColumnIndex(BASE_QUANTITY))
                val unitPrice = cursor.getInt(cursor.getColumnIndex(UNIT_PRICE))
                val receiptDate = cursor.getString(cursor.getColumnIndex(RECEIPT_DATE))
                val date = cursor.getString(cursor.getColumnIndex(BASE_DATE))
                val categoryId = cursor.getLong(cursor.getColumnIndex(PRODUCT_CATEGORY_ID_PRODUCT))

                val product = Product(id, name, unit, quantity, unitPrice, receiptDate, date, categoryId)
                productList.add(product)
            }
        }
        cursor.close()
        db.close()
        return productList
    }

    fun findAllShopping(condition: String = ""): MutableList<ShoppingList> {
        val shoppingList = mutableListOf<ShoppingList>()
        val db = readableDatabase
        var selectALLQuery = "SELECT * FROM $TABLE_NAME_SHOPPING ORDER BY $BOUGHT, $BASE_NAME"
        if (condition.isNotEmpty()) {
            selectALLQuery = "SELECT * FROM $TABLE_NAME_SHOPPING WHERE $condition"
        }
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val quantity = cursor.getInt(cursor.getColumnIndex(BASE_QUANTITY))
                val unit = cursor.getString(cursor.getColumnIndex(UNIT))
                val bought = cursor.getString(cursor.getColumnIndex(BOUGHT))!!.toBoolean()
                var productId = (-1).toLong()
                if (!cursor.isNull(cursor.getColumnIndex(PRODUCT_ID_SHOPPING))) {
                    productId = cursor.getLong(cursor.getColumnIndex(PRODUCT_ID_SHOPPING))
                }
                val shopping = ShoppingList(id, name, quantity, unit, bought, productId)
                shoppingList.add(shopping)
            }
        }
        cursor.close()
        db.close()
        return shoppingList
    }

    fun findAllReceipt(condition: String = ""): MutableList<Receipt> {
        val receiptList = mutableListOf<Receipt>()
        val db = readableDatabase
        var selectALLQuery = "SELECT * FROM $TABLE_NAME_RECEIPT"
        if (condition.isNotEmpty()) {
            selectALLQuery = "SELECT * FROM $TABLE_NAME_RECEIPT $condition"
        }
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val receiptId = cursor.getLong(cursor.getColumnIndex(RECEIPT_ID))
                val date = cursor.getString(cursor.getColumnIndex(BASE_DATE))
                val amount = cursor.getInt(cursor.getColumnIndex(BASE_AMOUNT))
                val comment = cursor.getString(cursor.getColumnIndex(BASE_COMMENT))
                val productId = cursor.getLong(cursor.getColumnIndex(PRODUCT_ID_RECEIPT))

                val receipt = Receipt(id, receiptId, date, amount, comment, productId)
                receiptList.add(receipt)
            }
        }
        cursor.close()
        db.close()
        return receiptList
    }
    fun updateReceipt(receipt: Receipt): Boolean {
        Log.i("Receipt", "db: $receipt")
        val db = writableDatabase
        val success = db.update(TABLE_NAME_RECEIPT,
            insertValuesReceipt(receipt),
            "$RECEIPT_ID = ? AND $PRODUCT_ID_RECEIPT = ?",
            arrayOf(receipt.baseID.toString(), receipt.productId.toString()))
        Log.i("Receipt", "db: updated row = $success")
        return success > -1
    }

    fun changeShoppingBought(element: ShoppingList) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME_SHOPPING SET $BOUGHT='${element.isBought}' WHERE id=${element.id}")
    }

    fun insertLimit(amount: Int, day: Int) {
        val db = this.writableDatabase
        val selection = "$BASE_ID=1"
        val values = ContentValues()
        values.put(BASE_AMOUNT, amount)
        values.put(CURRENT_DAY_LIMIT, day)
        db.update(TABLE_NAME_LIMIT, values, selection, null)
        db.close()
    }

    fun insertDayToLimit(day: Int) {
        val db = this.writableDatabase
        val selection = "$BASE_ID=1"
        val values = ContentValues()
        values.put(CURRENT_DAY_LIMIT, day)
        db.update(TABLE_NAME_LIMIT, values, selection, null)
        db.close()
    }

    fun dayIsNotEquals(currentDay: Int): Boolean {
        val db = readableDatabase
        val selectMaxQuery = "SELECT $CURRENT_DAY_LIMIT FROM $TABLE_NAME_LIMIT"
        val cursor = db.rawQuery(selectMaxQuery, null)
        var day = 0
        with(cursor) {
            while (moveToNext()) {
                day = cursor.getInt(0)
            }
        }
        cursor.close()
        db.close()
        return day != currentDay
    }

    fun limitsIsNotEquals(limit: Int): Boolean {
        return getCurrentLimit() != limit
    }

    fun getCurrentLimit(): Int {
        val db = readableDatabase
        val selectAmountQuery = "SELECT $BASE_AMOUNT FROM $TABLE_NAME_LIMIT"
        val cursor = db.rawQuery(selectAmountQuery, null)
        var amount = 0
        with(cursor) {
            while (moveToNext()) {
                amount = cursor.getInt(0)
            }
        }
        cursor.close()
        db.close()
        return amount
    }

    private fun limitReduction(amount: Int) {
        if (!dayIsNotEquals(getCurrentDay())) {
            val dbLimit = getCurrentLimit()
            val currentLimit = dbLimit + amount
            insertLimit(currentLimit, getCurrentDay())
            limitLogger()
        }
    }

    private fun limitLogger() {
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_LIMIT"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val amount = cursor.getInt(cursor.getColumnIndex(BASE_AMOUNT))
                val day = cursor.getInt(cursor.getColumnIndex(CURRENT_DAY_LIMIT))
                Log.i("limit", "find: \n id=$id, amount=$amount, day=$day")
            }
        }
        cursor.close()
        db.close()
    }

    fun isHarmfulCategory(productCategoryId: Long): Boolean {
        var result = false
        val db = readableDatabase
        val selectALLQuery =
            "SELECT * FROM $TABLE_NAME_PRODUCT_CATEGORY WHERE $PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY = 1"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val productPropertyValue = cursor.getString(cursor.getColumnIndex(PRODUCT_PROPERTY_VALUE))
                if (id == productCategoryId && productPropertyValue == "true") {
                    result = true
                }
            }
        }
        cursor.close()
        db.close()
        return result
    }

    fun deleteByPosition(position: Long, TABLE_NAME: String) {
        val db = readableDatabase
        val selection = "id LIKE ?"
        val selectionArgs = arrayOf("$position")
        Log.i("db", "DELETE: $position")
        db.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun deleteByReceiptByReceiptId(receiptID: Long, TABLE_NAME: String) {
        val db = readableDatabase
        val selection = "$RECEIPT_ID = ?"
        val selectionArgs = arrayOf("$receiptID")
        Log.i("db", "DELETE: $receiptID")
        db.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun deleteTableContent(TABLE_NAME: String) {
        readableDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }

    private fun dropTable(TABLE_NAME: String) {
        readableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(readableDatabase)
    }

    private fun insertValuesDream(it: Dream): ContentValues {
        val values = ContentValues()
        values.put(BASE_NAME, it.name)
        values.put(BASE_AMOUNT, it.amount)
        values.put(WHERE_DREAM, it.where)
        return values
    }

    private fun insertValuesProductProperty(it: ProductProperty): ContentValues {
        val values = ContentValues()
        values.put(BASE_NAME, it.name)
        values.put(TYPE_PRODUCT_PROPERTY, it.type)
        return values
    }

    private fun insertValuesTransaction(it: Transaction): ContentValues {
        val values = ContentValues()
        values.put(BASE_AMOUNT, it.amount)
        values.put(BASE_DATE, it.date)
        values.put(BASE_NAME, it.name)
        values.put(BASE_COMMENT, it.comment)
        values.put(FREQUENCY_TRANSACTION, it.frequency)
        return values
    }

    private fun insertValuesProductCategory(it: ProductCategory): ContentValues? {
        val values = ContentValues()
        values.put(BASE_NAME, it.name)
        values.put(PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY, it.productPropertyId)
        values.put(PRODUCT_PROPERTY_VALUE, it.productPropertyValue)
        return values
    }

    private fun insertValuesProduct(it: Product): ContentValues {
        val values = ContentValues()
        values.put(BASE_NAME, it.name)
        values.put(UNIT, it.unit)
        values.put(BASE_QUANTITY, it.quantity)
        values.put(UNIT_PRICE, it.unitPrice)
        values.put(RECEIPT_DATE, it.receiptDate)
        values.put(BASE_DATE, it.date)
        values.put(PRODUCT_CATEGORY_ID_PRODUCT, it.categoryId)
        return values
    }

    private fun insertValuesShoppingList(it: ShoppingList): ContentValues {
        val values = ContentValues()
        values.put(BASE_NAME, it.name)
        values.put(BASE_QUANTITY, it.quantity)
        values.put(UNIT, it.unit)
        values.put(BOUGHT, it.isBought)
        if(it.productId != (-1).toLong()) {
            values.put(PRODUCT_ID_SHOPPING, it.productId)
        }
        return values
    }

    private fun insertValuesReceipt(it: Receipt): ContentValues {
        val values = ContentValues()
        values.put(BASE_DATE, it.date)
        values.put(RECEIPT_ID, it.baseID)
        values.put(BASE_AMOUNT, it.amount)
        values.put(BASE_COMMENT, it.comment)
        if (it.productId != (-1).toLong()) {
            values.put(PRODUCT_ID_RECEIPT, it.productId)
        }
        return values
    }

    fun insertTestdata() {
        deleteTableContent(TABLE_NAME_PRODUCT_PROPERTY)
        deleteTableContent(TABLE_NAME_PRODUCT_CATEGORY)
        deleteTableContent(TABLE_NAME_PRODUCT)
        deleteTableContent(TABLE_NAME_SHOPPING)
        deleteTableContent(TABLE_NAME_TRANSACTION)
        deleteTableContent(TABLE_NAME_RECEIPT)
        deleteTableContent(TABLE_NAME_DREAM)


        ProductPropertyEnum.values().forEach {
            val productProperty = ProductProperty(it.propertyName, it.type)
            insert(productProperty)
        }
        Log.i("testData", "ProductProperty: ")
        findAllProductProperty().forEach {
            Log.i("testData", it.toString())
        }

        ProductCategoryEnum.values().forEach {
            val category = ProductCategory(it.categoryName, it.propertyId.toLong(), it.propertyValue)
            insert(category)
        }
        Log.i("testData", "ProductCategory: ")
        findAllProductCategory().forEach {
            Log.i("testData", it.toString())
        }

        var product = Product("alma", "db", 3, 120, categoryId = 4)
        insert(product)
        product = Product("Alkohol", "Liter", 2, 1200, categoryId = 4)
        insert(product)
        product = Product("Alkohol", "Liter", 2, 1200, categoryId = 4)
        insert(product)
        product = Product("Sör", "Liter", 6, 250, categoryId = 4)
        insert(product)
        product = Product("Cif", "db", 1, 750, categoryId = 10)
        insert(product)
        product = Product("Ragasztó", "db", 4, 350, categoryId = 8)
        insert(product)
        product = Product("Hagyma", "db", 4, 50, categoryId = 1)
        insert(product)
        product = Product("Mackó nadrág", "db", 1, 2500, categoryId = 5)
        insert(product)
        product = Product("Ropi", "db", 1, 500, categoryId = 2)
        insert(product)
        product = Product("Pepsi", "db", 1, 500, categoryId = 3)
        insert(product)
        product = Product("Csőfogó", "db", 1, 1500, categoryId = 7)
        insert(product)

        product = Product("alma", "db", 3, 120, receiptDate = "2019-04-25", categoryId = 4)
        insert(product)
        product = Product("alma", "db", 3, 120, receiptDate = "2019-04-24", categoryId = 4)
        insert(product)
        product = Product("alma", "db", 3, 120, receiptDate = "2019-04-26", categoryId = 4)
        insert(product)
        product = Product("alma", "db", 3, 120, receiptDate = "2019-04-23", categoryId = 4)
        insert(product)
        product = Product("kenyer", "db", 3, 120, receiptDate = "2019-04-22", categoryId = 4)
        insert(product)
        product = Product("kenyer", "db", 3, 120, receiptDate = "2019-04-21", categoryId = 4)
        insert(product)

        Log.i("testData", "Product: ")
        findAllProduct().forEach {
            Log.i("testData", it.toString())
        }

        var transaction = Transaction(1000, "2019-04-25", "Zsebpénz")
        insert(transaction)
        transaction = Transaction(1000, "2019-04-16", "Bor", "Elfogyott :(")
        insert(transaction)
        transaction = Transaction(100000, "2019-04-10", "Fizetés", frequency = "Havonta")
        insert(transaction)
        transaction = Transaction(-2000, "2019-04-11", "Gyerektartás", frequency = "Heti")
        insert(transaction)
        transaction = Transaction(1030, "2019-04-15", "Gáz", frequency = "Havonta")
        insert(transaction)
        transaction = Transaction(1500, "2019-04-15", "Kutya kaja", frequency = "Heti")
        insert(transaction)
        Log.i("testData", "Transaction: ")
        findAllTransaction().forEach {
            Log.i("testData", it.toString())
        }

        var receipt = Receipt(1, "2019-05-03", 13000, "Ikea", 1)
        insert(receipt)
        receipt = Receipt(1, "2019-05-03", 13000, "Ikea", 2)
        insert(receipt)
        receipt = Receipt(2, "2019-05-02", 5000, "asd", 3)
        insert(receipt)
        Log.i("testData", "Receipt: ")
        findAllReceipt().forEach {
            Log.i("testData", it.toString())
        }

        var shoppingList = ShoppingList("kenyér", 2, "Db", true, 2)
        insert(shoppingList)
        shoppingList = ShoppingList("alma_db",5, "kg", productId = 1)
        insert(shoppingList)
        shoppingList = ShoppingList("bor", 10, "l", true,3)
        insert(shoppingList)

        val dreamsList = arrayListOf(
            Dream("Samsung HD Tv", 55000, "Media Markt"),
            Dream("Fűnyíró", 100000, "OBI")
        )
        inserts(dreamsList)
    }

    fun categoriesWithWarrantyAndHarmful(): ArrayList<Long> {
        val list = ArrayList<Long>()
        val db = readableDatabase
        val selectALLQuery =
            "SELECT * FROM $TABLE_NAME_PRODUCT_CATEGORY WHERE $PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY = 2 OR $PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY = 1"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val productPropertyValue = cursor.getString(cursor.getColumnIndex(PRODUCT_PROPERTY_VALUE))
                if (productPropertyValue == "true") {
                    val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                    list.add(id)
                }
            }
        }
        cursor.close()
        db.close()
        return list
    }

    companion object {
        private const val DB_NAME = "assistant.db"
        private const val DB_VERSION = 1

        const val BASE_ID = "id"
        const val BASE_NAME = "name"
        const val BASE_AMOUNT = "amount"
        const val BASE_QUANTITY = "quantity"
        const val BASE_DATE = "date"
        const val BASE_COMMENT = "comment"
        const val RECEIPT_DATE = "receipt_date"

        //transaction
        const val TABLE_NAME_TRANSACTION = "trans"
        const val FREQUENCY_TRANSACTION = "frequency"

        //product_property
        const val TABLE_NAME_PRODUCT_PROPERTY = "product_property"
        const val TYPE_PRODUCT_PROPERTY = "type"


        //dream
        const val TABLE_NAME_DREAM = "dream"
        const val WHERE_DREAM = "place"

        //product
        const val TABLE_NAME_PRODUCT = "product"
        const val UNIT_PRICE = "unit_price"
        const val UNIT = "unit"
        const val PRODUCT_CATEGORY_ID_PRODUCT = "product_category_id"

        //product_category
        const val TABLE_NAME_PRODUCT_CATEGORY = "product_category"
        const val PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY = "product_property_id"
        const val PRODUCT_PROPERTY_VALUE = "product_property_value"

        //receipt
        const val TABLE_NAME_RECEIPT = "receipt"
        const val RECEIPT_ID = "receipt_ID"
        const val PRODUCT_ID_RECEIPT = "product_id"

        //shopping_list
        const val TABLE_NAME_SHOPPING = "shopping_list"
        const val PRODUCT_ID_SHOPPING = "product_id"
        const val BOUGHT = "bought"

        //limit
        const val TABLE_NAME_LIMIT = "day_limit"
        const val CURRENT_DAY_LIMIT = "day"
        const val CREATE_TABLE_LIMIT = "CREATE TABLE IF NOT EXISTS ${DatabaseHandler.TABLE_NAME_LIMIT} " +
                "(${DatabaseHandler.BASE_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseHandler.BASE_AMOUNT} INTEGER, " +
                "${DatabaseHandler.CURRENT_DAY_LIMIT} INTEGER)"
        const val DEFAULT_LIMIT_AMOUNT = 25000
    }
}