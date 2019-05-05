package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.features.limit.setupLimit
import com.coderaptor.financial.assistant.app.util.SharedPreference
import com.coderaptor.financial.assistant.app.util.fieldsEmpty
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        editText.text = Editable.Factory.getInstance().newEditable(dbHandler.getCurrentLimit().toString())
        sporolas.isChecked = SharedPreference.saving
        bevasarlas.isChecked = SharedPreference.shoppingMonitor
        joslas.isChecked = SharedPreference.estimate
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        if (fieldsEmpty(editText.text)) {
            val limitAmount = editText.text.toString().toInt()
            setupLimit(limitAmount, dbHandler)
        }
        SharedPreference.saving = sporolas.isChecked
        SharedPreference.shoppingMonitor = bevasarlas.isChecked
        SharedPreference.estimate = joslas.isChecked
        dbHandler.close()
    }
}

