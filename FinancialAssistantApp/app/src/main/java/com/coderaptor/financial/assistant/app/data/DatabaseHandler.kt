package com.coderaptor.financial.assistant.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.coderaptor.financial.assistant.app.core.*
import com.coderaptor.financial.assistant.app.core.ProductProperty.Companion.CREATE_TABLE_PRODUCT_PROPERTY
import com.coderaptor.financial.assistant.app.core.Transaction.Companion.CREATE_TABLE_TRANSACTION
import com.coderaptor.financial.assistant.app.core.Dream.Companion.CREATE_TABLE_DREAM

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
        //RECEIPT
        //db.execSQL(Receipt.CREATE_TABLE_RECEIPT)
        //Log.i("db", "Create Receipt table done!")
        //SHOPPING
        //db.execSQL(ShoppingList.CREATE_TABLE_SHOPPING_LIST)
        //Log.i("db", "Create Shopping table done!")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteAll(TABLE_NAME_TRANSACTION)
        deleteAll(TABLE_NAME_PRODUCT_PROPERTY)
        deleteAll(TABLE_NAME_DREAM)
        onCreate(db)
    }

    fun <T> insert(it: T) {
        val db = this.writableDatabase
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
        }
        db.close()
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

    fun findAllTransaction(): MutableList<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_TRANSACTION"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val amount = cursor.getInt(cursor.getColumnIndex(BASE_AMOUNT))
                val date = cursor.getString(cursor.getColumnIndex(BASE_DATE))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val frequency = cursor.getString(cursor.getColumnIndex(FREQUENCY_TRANSACTION))

                val transaction = Transaction(id, amount, date, name, frequency)
                transactionList.add(transaction)
            }
        }
        cursor.close()
        db.close()
        return transactionList
    }

    fun findAllProductProperty(): MutableList<ProductProperty> {
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

    fun findAllProduct(): MutableList<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_PRODUCT"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(BASE_ID))
                val name = cursor.getString(cursor.getColumnIndex(BASE_NAME))
                val unit = cursor.getString(cursor.getColumnIndex(UNIT))
                val quantity = cursor.getInt(cursor.getColumnIndex(BASE_QUANTITY))
                val unitPrice = cursor.getInt(cursor.getColumnIndex(UNIT_PRICE))
                val categoryId = cursor.getLong(cursor.getColumnIndex(PRODUCT_CATEGORY_ID_PRODUCT))


                val product = Product(id, name, unit, quantity, unitPrice, categoryId)
                productList.add(product)
            }
        }
        cursor.close()
        db.close()
        return productList
    }


    fun deleteByPosition(position: Long, TABLE_NAME: String) {
        val db = readableDatabase
        val selection = "id LIKE ?"
        val selectionArgs = arrayOf("$position")
        Log.i("db", "DELETE: $position")
        db.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun deleteAll(TABLE_NAME: String) {
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
        values.put(PRODUCT_CATEGORY_ID_PRODUCT, it.categoryId)
        return values
    }

    fun insertTestdata() {

    }

    companion object {
        private const val DB_NAME = "assistant.db"
        private const val DB_VERSION = 1

        const val BASE_ID = "id"
        const val BASE_NAME = "name"
        const val BASE_AMOUNT = "amount"
        const val BASE_QUANTITY = "quantity"
        const val BASE_DATE = "date"

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
        const val PRODUCT_CATEGORY_ID_PRODUCT="product_category_id"

        //product_category
        const val TABLE_NAME_PRODUCT_CATEGORY = "product_category"
        const val PRODUCT_PROPERTY_ID_PRODUCT_CATEGORY="product_property_id"
        const val PRODUCT_PROPERTY_VALUE= "product_property_value"

        //receipt
        const val TABLE_NAME_RECEIPT = "receipt"
        const val PRODUCT_ID_RECEIPT = "product_id"

        //shopping_list
        const val TABLE_NAME_SHOPPING = "shopping_list"
        const val PRODUCT_ID_SHOPPING = "product_id"
    }
}