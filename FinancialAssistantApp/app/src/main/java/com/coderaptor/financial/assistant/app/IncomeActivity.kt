package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.datetime.datePicker
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.SharedPreference
import com.coderaptor.financial.assistant.app.util.formatDate
import com.coderaptor.financial.assistant.app.util.spinner.StringWithId
import com.coderaptor.financial.assistant.app.util.spinner.getCategoryStringWithIdList
import com.coderaptor.financial.assistant.app.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_income.*
import kotlinx.android.synthetic.main.content_receipt.*
import kotlinx.android.synthetic.main.content_receipt.amountField
import kotlinx.android.synthetic.main.dialog_add_income.*
import kotlinx.android.synthetic.main.dialog_add_income.categoryField
import kotlinx.android.synthetic.main.dialog_add_income.dateField
import kotlinx.android.synthetic.main.dialog_add_product.*

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

        dateField.isClickable = true
        dateField.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
        dateField.setOnClickListener {
            dateClick(this)
        }

        fab.setOnClickListener {
            val result = fieldsEmpty(amountField.text)

            if (result){

                var amount: Int = amountField.text.toString().toInt()
                if (kiadas.isChecked) amount = amountField.text.toString().toInt() * -1
                val date = dateField.text.toString()
                val category: String = categoryField.selectedItem.toString()

                val transaction = Transaction(amount, date, category)

                dbHandler.insert(transaction)
                if (dbHandler.getCurrentLimit() < 0) {
                    toast("Napi limit összeg meghaladva")
                }

                toast("sikeres hozzáadás")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                toast("Hiányzó adat!")
            }
        }
    }

    fun dateClick(context: IncomeActivity) {
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
