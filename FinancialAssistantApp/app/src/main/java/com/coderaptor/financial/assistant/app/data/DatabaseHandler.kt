package com.coderaptor.financial.assistant.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.coderaptor.financial.assistant.app.core.ProductProperty
import com.coderaptor.financial.assistant.app.core.ProductProperty.Companion.CREATE_TABLE_PRODUCT_PROPERTY
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.core.Transaction.Companion.CREATE_TABLE_TRANSACTION

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        //TRANSACTION
        db.execSQL(CREATE_TABLE_TRANSACTION)
        Log.i("db", "Create Transaction table done!")
        //PRODUCT_PROPERTY
        db.execSQL(CREATE_TABLE_PRODUCT_PROPERTY)
        Log.i("db", "Create ProductProperty table done!")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteAll(TABLE_NAME_TRANSACTION)
        deleteAll(TABLE_NAME_PRODUCT_PROPERTY)
        onCreate(db)
    }

    fun <T> insert(it: T) {
        val db = this.writableDatabase
        val values = ContentValues()
        if (it is Transaction) {
            values.put(AMOUNT_TRANSACTION, it.amount)
            values.put(DATE_TRANSACTION, it.date)
            values.put(NAME_BASE, it.name)
            values.put(FREQUENCY_TRANSACTION, it.frequency)
            db.insert(TABLE_NAME_TRANSACTION, null, values)
        }else if (it is ProductProperty) {
            values.put(NAME_BASE, it.name)
            values.put(TYPE_PRODUCT_PROPERTY, it.type)
            db.insert(TABLE_NAME_PRODUCT_PROPERTY, null, values)
            Log.i("db", ":)")
        }
        db.close()
    }

    fun <T> inserts(myList: ArrayList<T>) {
        val db = this.writableDatabase
        myList.forEach {
            val values = ContentValues().apply {
                if (it is Transaction) {
                    put(AMOUNT_TRANSACTION, it.amount)
                    put(DATE_TRANSACTION, it.date)
                    put(NAME_BASE, it.name)
                    put(FREQUENCY_TRANSACTION, it.frequency)
                }
            }
            db.insert(TABLE_NAME_TRANSACTION, null, values)
        }
        db.close()
    }

    fun findAllTransaction(): MutableList<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_TRANSACTION"
        val cursor = db.rawQuery(selectALLQuery, null)
        with(cursor) {
            while (moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(ID_BASE))
                val amount = cursor.getInt(cursor.getColumnIndex(AMOUNT_TRANSACTION))
                val date = cursor.getString(cursor.getColumnIndex(DATE_TRANSACTION))
                val name = cursor.getString(cursor.getColumnIndex(NAME_BASE))
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
                val id = cursor.getLong(cursor.getColumnIndex(ID_BASE))
                val name = cursor.getString(cursor.getColumnIndex(NAME_BASE))
                val type = cursor.getString(cursor.getColumnIndex(TYPE_PRODUCT_PROPERTY))

                val property = ProductProperty(id, name, type)
                propertyList.add(property)
            }
        }
        cursor.close()
        db.close()
        return propertyList
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

    companion object {
        private const val DB_NAME = "assistant.db"
        private const val DB_VERSION = 1

        const val ID_BASE = "id"
        const val NAME_BASE = "name"

        //transaction
        const val TABLE_NAME_TRANSACTION = "trans"
        const val AMOUNT_TRANSACTION = "amount"
        const val DATE_TRANSACTION = "date"
        const val FREQUENCY_TRANSACTION = "frequency"

        //product_property
        const val TABLE_NAME_PRODUCT_PROPERTY = "product_property"
        const val TYPE_PRODUCT_PROPERTY = "type"

        //product
        private const val TABLE_NAME_PRODUCT = "product"

        //shopping_list
        private const val TABLE_NAME_SHOPPING = "shopping_list"
        private const val PRODUCT_ID_SHOPPING = "product_id"
        private const val QUANTITY_SHOPPING = "quantity"
    }
}