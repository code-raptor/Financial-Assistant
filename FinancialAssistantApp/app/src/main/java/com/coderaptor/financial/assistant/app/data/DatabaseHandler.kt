package com.coderaptor.financial.assistant.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.coderaptor.financial.assistant.app.core.Income

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($ID Integer PRIMARY KEY, $AMOUNT TEXT, $DATE TEXT, $CATEGORY TEXT, $FREQUENCY TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertIncome(income: Income): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(AMOUNT, income.amount)
        values.put(DATE, income.date)
        values.put(CATEGORY, income.category)
        values.put(FREQUENCY, income.frequency)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.i("db", "$success")
        Log.d("db", "$success")
        return (Integer.parseInt("$success") != -1)
    }

    fun findAllIncome(): String {
        var allIncome = ""
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex(ID))
                    val amount = cursor.getString(cursor.getColumnIndex(AMOUNT))
                    val date = cursor.getString(cursor.getColumnIndex(DATE))
                    val category = cursor.getString(cursor.getColumnIndex(CATEGORY))
                    val frequency = cursor.getString(cursor.getColumnIndex(FREQUENCY))

                    allIncome = "$allIncome\n$id $amount $date $category $frequency"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allIncome
    }



    fun deleteAll() {
        readableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(readableDatabase)
    }

    companion object {
        private const val DB_NAME = "assistant"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "income"
        private const val ID = "id"
        private const val AMOUNT = "amount"
        private const val DATE = "date"
        private const val CATEGORY = "category"
        private const val FREQUENCY = "frequency"
    }
}