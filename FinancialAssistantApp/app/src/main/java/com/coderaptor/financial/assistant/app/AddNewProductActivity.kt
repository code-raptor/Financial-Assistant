package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.spinner.StringWithId
import com.coderaptor.financial.assistant.app.util.spinner.getCategoryStringWithIdList
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

        val categoryIdWithWarranty = dbHandler.categoriesWithWarranty()
        categoryField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val swt = parent.selectedItem as StringWithId
                categoryId = swt.id
                if (categoryIdWithWarranty.contains(categoryId)) {
                    end_dateField.visibility = View.VISIBLE
                    label.visibility = View.VISIBLE
                    warranty.isChecked = true
                }else {
                    end_dateField.visibility = View.INVISIBLE
                    label.visibility = View.INVISIBLE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        warranty.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                end_dateField.visibility = View.VISIBLE
                label.visibility = View.VISIBLE
            }else {
                end_dateField.visibility = View.INVISIBLE
                label.visibility = View.INVISIBLE
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener {
            val name = productName.text.toString()
            val quantity = if (quantityField.text.isNotEmpty())quantityField.text.toString().toInt() else 1
            val unit = unitField.selectedItem.toString()
            val price = priceField.text.toString().toInt()
            val warranty = warranty.isChecked
            if (warranty) {
                val endDate = end_dateField.toString()
            }

            val product = Product(name, unit, quantity, price, categoryId)

            dbHandler.insert(product)

            Toast.makeText(this, "Sikeres hozzáadás", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }
    }
}