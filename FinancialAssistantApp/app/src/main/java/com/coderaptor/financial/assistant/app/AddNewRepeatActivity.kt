package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.RepeatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_newrepeat.*
import kotlinx.android.synthetic.main.content_newrepeat_transaction.*

class AddNewRepeatActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newrepeat)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener {
            val intent = Intent(this, RepeatActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener { view ->
            Snackbar.make(view, "Replace rögzit", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val amountField : EditText = findViewById(R.id.amountField)
            val dateField: EditText = findViewById(R.id.dateField)

            var amount: Int = amountField.text.toString().toInt()
            if (kiadas.isChecked) amount = amountField.text.toString().toInt() * -1
            val date: String = dateField.text.toString()
            val category: String = categoryField.selectedItem.toString()
            val frequency: String = frequencyField.selectedItem.toString()

            val transaction = Transaction(amount, date, category, frequency)

            dbHandler.insert(transaction)
            Toast.makeText(this, "Sikeres hozzáadás", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}