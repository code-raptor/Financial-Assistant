package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.RepeatActivity
import com.coderaptor.financial.assistant.app.util.formatDate
import com.coderaptor.financial.assistant.app.util.toast
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

        dateField.isClickable = true
        dateField.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
        dateField.setOnClickListener {
            dateClick(this)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace rögzit", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val result = fieldsEmpty(amountField.text)

            if(result) {
                val amountField: EditText = findViewById(R.id.amountField)
                val dateField: EditText = findViewById(R.id.dateField)

                var amount: Int = amountField.text.toString().toInt()
                if (kiadas.isChecked) amount = amountField.text.toString().toInt() * -1
                val date = dateField.text.toString()
                val category: String = categoryField.selectedItem.toString()
                val frequency: String = frequencyField.selectedItem.toString()

                val transaction = Transaction(amount, date, category, frequency)

                dbHandler.insert(transaction)
                Toast.makeText(this, "Sikeres hozzáadás", Toast.LENGTH_SHORT).show()
                if (dbHandler.getCurrentLimit() < 0) {
                    Toast.makeText(this, "Napi limit összeg meghaladva", Toast.LENGTH_LONG).show()
                }

                val intent = Intent(this, RepeatActivity::class.java)
                startActivity(intent)
            }
            else{
                toast("Hiányzó adat!")
            }
        }
    }

    fun dateClick(context: AddNewRepeatActivity) {
        MaterialDialog(this).show {
            setTheme(R.style.AppTheme)
            datePicker { _, innerDate ->
                context.dateField.text = Editable.Factory.getInstance().newEditable(innerDate.formatDate())
            }
        }
    }

    fun fieldsEmpty(vararg fields: Editable):Boolean{
        for (data in fields){
            if(data.isEmpty()){
                return false
            }
        }
        return true
    }
}