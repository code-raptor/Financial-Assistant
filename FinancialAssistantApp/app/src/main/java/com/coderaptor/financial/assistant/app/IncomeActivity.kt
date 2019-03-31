package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler

import kotlinx.android.synthetic.main.activity_income.*
import kotlinx.android.synthetic.main.content_income.*

class IncomeActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        button.setOnClickListener { view ->
            Snackbar.make(view, "Replace rögzit", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val amountField : EditText = findViewById(R.id.amountField)
            val dateField: EditText = findViewById(R.id.dateField)
            val categoryField: Spinner = this.findViewById(R.id.categoryField)
            val frequencyField: Spinner = findViewById(R.id.frequencyField)

            val amount: Int = amountField.text.toString().toInt()
            val date: String = dateField.text.toString()
            val category: String = categoryField.selectedItem.toString()
            val frequency: String = frequencyField.selectedItem.toString()
            Log.i("transaction", "$amount")
            Log.i("transaction", date)
            Log.i("transaction", category)
            Log.i("transaction", frequency)

            val income = Transaction(1, amount, date, category, frequency)

            dbHandler.insert(income)
            Toast.makeText(this, "Sikerek hozzáadás", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
