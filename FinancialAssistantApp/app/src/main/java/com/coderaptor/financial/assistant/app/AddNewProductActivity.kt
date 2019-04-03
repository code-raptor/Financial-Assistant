package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coderaptor.financial.assistant.app.core.Dream
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_newdream.*
import kotlinx.android.synthetic.main.content_newdream.*

class AddNewProductActivity: AppCompatActivity() {
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newproduct)

        back.setOnClickListener {
            Log.i("click", "go receiptActivity")
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener { view ->
            Snackbar.make(view, "Replace rögzit", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val amountField: EditText = findViewById(R.id.amountField)
            val nameField: EditText = findViewById(R.id.nameField)
            val whereField: EditText = findViewById(R.id.whereField)

            val amount = amountField.text.toString().toInt()
            val name = nameField.text.toString()
            val where = whereField.text.toString()

            val dream = Dream(name, amount, where)

            dbHandler.insert(dream)

            Toast.makeText(this, "Sikeres hozzáadás", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)

        }
    }
}