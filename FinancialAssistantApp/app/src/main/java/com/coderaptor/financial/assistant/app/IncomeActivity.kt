package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
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
        supportActionBar?.setDisplayShowTitleEnabled(false)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener {
            val amountField : EditText = findViewById(R.id.amountField)
            val dateField: EditText = findViewById(R.id.dateField)


            var amount: Int = amountField.text.toString().toInt()
            if (kiadas.isChecked) amount = amountField.text.toString().toInt() * -1
            val date: String = dateField.text.toString()
            val category: String = categoryField.selectedItem.toString()

            val transaction = Transaction(amount, date, category)

            dbHandler.insert(transaction)
            if (dbHandler.getCurrentLimit() < 0) {
                Toast.makeText(this, "Napi limit összeg meghaladva", Toast.LENGTH_LONG).show()
            }

            Toast.makeText(this, "Sikeres hozzáadás", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
