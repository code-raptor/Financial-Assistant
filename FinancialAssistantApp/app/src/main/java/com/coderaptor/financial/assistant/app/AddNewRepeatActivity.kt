package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AddNewRepeatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newrepeat)

        val back: ImageButton = findViewById(R.id.back)
        Log.i("click", "back")
        back.setOnClickListener {
            Log.i("click", "go main")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}