package com.coderaptor.financial.assistant.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.coderaptor.financial.assistant.app.core.Transaction

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_TRANSACTION = "CREATE TABLE $TABLE_NAME_TRANSACTION " +
                "($ID_TRANSACTION INTEGER PRIMARY KEY, $AMOUNT_TRANSACTION INTEGER, $DATE_TRANSACTION TEXT, $CATEGORY_TRANSACTION TEXT, $FREQUENCY_TRANSACTION TEXT)"
        db.execSQL(CREATE_TABLE_TRANSACTION)

        /*val CREATE_TABLE_SHOPPING = "CREATE TABLE $TABLE_NAME_SHOPPING " +
                "($ID_SHOPPING INTEGER PRIMARY KEY, $PRODUCT_ID_SHOPPING INTEGER, $QUANTITY_SHOPPING INTEGER," +
                "FOREIGN KEY($PRODUCT_ID_SHOPPING) REFERENCES $TABLE_NAME_PRODUCT($ID_PRODUCT))"
        db?.execSQL(CREATE_TABLE_SHOPPING)*/
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteAll("trans")
        onCreate(db)
    }

    fun insertTransaction(transaction: Transaction): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(AMOUNT_TRANSACTION, transaction.amount)
        values.put(DATE_TRANSACTION, transaction.date)
        values.put(CATEGORY_TRANSACTION, transaction.category)
        values.put(FREQUENCY_TRANSACTION, transaction.frequency)
        val success = db.insert(TABLE_NAME_TRANSACTION, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun insertTransactions(transactions: MutableList<Transaction>) {
        val db = this.writableDatabase
        transactions.forEach {
            val values = ContentValues().apply {
                put(AMOUNT_TRANSACTION, it.amount)
                put(DATE_TRANSACTION, it.date)
                put(CATEGORY_TRANSACTION, it.category)
                put(FREQUENCY_TRANSACTION, it.frequency)
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
            while(moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(ID_TRANSACTION))
                val amount = cursor.getInt(cursor.getColumnIndex(AMOUNT_TRANSACTION))
                val date = cursor.getString(cursor.getColumnIndex(DATE_TRANSACTION))
                val category = cursor.getString(cursor.getColumnIndex(CATEGORY_TRANSACTION))
                val frequency = cursor.getString(cursor.getColumnIndex(FREQUENCY_TRANSACTION))

                val transaction = Transaction(id,amount, date, category, frequency)
                transactionList.add(transaction)
            }
        }
        cursor.close()
        db.close()
        return transactionList
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
        private const val TABLE_NAME_TRANSACTION = "trans"
        private const val ID_TRANSACTION = "id"
        private const val AMOUNT_TRANSACTION = "amount"
        private const val DATE_TRANSACTION = "date"
        private const val CATEGORY_TRANSACTION = "category"
        private const val FREQUENCY_TRANSACTION = "frequency"

        //product
        private const val TABLE_NAME_PRODUCT = "product"
        private const val ID_PRODUCT = "id"

        //shopping_list
        private const val TABLE_NAME_SHOPPING = "shopping_list"
        private const val ID_SHOPPING = "id"
        private const val PRODUCT_ID_SHOPPING = "product_id"
        private const val QUANTITY_SHOPPING = "quantity"

    }
}