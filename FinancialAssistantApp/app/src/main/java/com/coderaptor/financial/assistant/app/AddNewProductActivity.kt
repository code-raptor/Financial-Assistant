package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.SharedPreference
import com.coderaptor.financial.assistant.app.util.formatDate
import com.coderaptor.financial.assistant.app.util.spinner.StringWithId
import com.coderaptor.financial.assistant.app.util.spinner.getCategoryStringWithIdList
import com.coderaptor.financial.assistant.app.util.toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_newproduct.*
import kotlinx.android.synthetic.main.content_product.*


class AddNewProductActivity: AppCompatActivity() {
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newproduct)

        val adapter = ArrayAdapter<StringWithId>(this, android.R.layout.simple_spinner_item, getCategoryStringWithIdList(dbHandler))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        categoryField.adapter = adapter
        var categoryId = 1.toLong()

        val categoryIdWithWarrantyAndHarmful = dbHandler.categoriesWithWarrantyAndHarmful()
        categoryField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val swt = parent.selectedItem as StringWithId
                categoryId = swt.id
                if (categoryIdWithWarrantyAndHarmful.contains(categoryId) && !dbHandler.isHarmfulCategory(categoryId)) {
                    dateField.visibility = View.VISIBLE
                    label.visibility = View.VISIBLE
                    warranty.isChecked = true
                }else if (!categoryIdWithWarrantyAndHarmful.contains(categoryId)) {
                    dateField.visibility = View.INVISIBLE
                    label.visibility = View.INVISIBLE
                    warranty.isChecked = false
                }else if (categoryIdWithWarrantyAndHarmful.contains(categoryId) && dbHandler.isHarmfulCategory(categoryId)) {
                    if (SharedPreference.shoppingMonitor) {
                        Snackbar.make(view, "Káros Termék", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        warranty.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                dateField.visibility = View.VISIBLE
                label.visibility = View.VISIBLE
            }else {
                dateField.visibility = View.INVISIBLE
                label.visibility = View.INVISIBLE
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }

        dateField.isClickable = true
        dateField.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
        dateField.setOnClickListener {
            dateClick(this)
        }

        fab.setOnClickListener {
            val name = productName.text.toString()
            val quantity = if (quantityField.text.isNotEmpty())quantityField.text.toString().toInt() else 1
            val unit = unitField.selectedItem.toString()
            val price = priceField.text.toString().toInt()
            val warranty = warranty.isChecked
            var product = Product(name, unit, quantity, price, categoryId = categoryId)
            if (warranty) {
                if (dateField.text.isNotEmpty()) {
                    val endDate = dateField.text.toString()
                    product = Product(name, unit, quantity, price, endDate, categoryId)
                }
            }

            dbHandler.insert(product)
            toast("Sikeres hozzáadás")

            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }
    }

    fun dateClick(context: AddNewProductActivity) {
        MaterialDialog(this).show {
            setTheme(R.style.AppTheme)
            datePicker { _, innerDate ->
                context.dateField.text = Editable.Factory.getInstance().newEditable(innerDate.formatDate())
            }
        }
    }
}