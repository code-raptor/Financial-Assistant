package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.features.limit.setupLimit
import kotlinx.android.synthetic.main.activity_settings.*



class SettingsActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        aboutButton.setOnClickListener {
        }
    }

    override fun onStop() {
        super.onStop()
        if (editText.text.isNotEmpty()) {
            val limitAmount = editText.text.toString().toInt()
            setupLimit(limitAmount, dbHandler)
        }
    }
}

