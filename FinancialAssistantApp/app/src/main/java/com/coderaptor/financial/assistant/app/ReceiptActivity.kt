package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReceiptActivity: AppCompatActivity() {
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            Log.i("click", "go mainForm")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val addnewproduct = findViewById<FloatingActionButton>(R.id.addnewproduct)
        addnewproduct.setOnClickListener {
            Log.i("click", "go AddNewProduct")
            val intent = Intent(this, AddNewProductActivity::class.java)
            startActivity(intent)
        }
    }

}