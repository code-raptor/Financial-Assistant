package com.coderaptor.financial.assistant.app.gui.dialogs

import android.app.Activity
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.recyclical.datasource.DataSource
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.*
import com.coderaptor.financial.assistant.app.util.spinner.StringWithId
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_add_product.*
import kotlinx.android.synthetic.main.dialog_add_product.view.*
import java.util.*

fun Activity.openProductDialog(dataSource: DataSource<Product>, dbHandler: DatabaseHandler, editData: Product? = null, adapter: ArrayAdapter<StringWithId>) {
    MaterialDialog(this).show {
        setTheme(R.style.AppTheme)
        title(R.string.product_string)
        customView(R.layout.dialog_add_product, scrollable = true)

        categoryField.adapter = adapter

        var categoryId = (-1).toLong()
        val categoryIdWithWarrantyAndHarmful = dbHandler.categoriesWithWarrantyAndHarmful()
        categoryField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val swt = parent.selectedItem as StringWithId
                categoryId = swt.id
                if (categoryIdWithWarrantyAndHarmful.contains(categoryId) && !dbHandler.isHarmfulCategory(categoryId)) {
                    dateField.visibility = View.VISIBLE
                    label.visibility = View.VISIBLE
                    warranty.isChecked = true
                } else if (!categoryIdWithWarrantyAndHarmful.contains(categoryId)) {
                    dateField.visibility = View.INVISIBLE
                    label.visibility = View.INVISIBLE
                    warranty.isChecked = false
                } else if (categoryIdWithWarrantyAndHarmful.contains(categoryId) && dbHandler.isHarmfulCategory(categoryId)) {
                    if (SharedPreference.shoppingMonitor) {
                        Snackbar.make(view, "Káros Termék", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val datefield = getCustomView().dateField
        datefield.isClickable = true
        datefield.text = Editable.Factory.getInstance().newEditable(Calendar.getInstance().formatDate())
        datefield.setOnClickListener {
            openCalendar(datefield)
        }

        warranty.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dateField.visibility = View.VISIBLE
                label.visibility = View.VISIBLE
            } else {
                dateField.visibility = View.INVISIBLE
                label.visibility = View.INVISIBLE
            }
        }

        if (editData != null) {
            categoryField.setSelection(editData.categoryId.toInt()-1)

            var counter = -1
            val unitList = resources.getStringArray(R.array.egyseg_array).toList()
            for (item in unitList) {
                counter++
                if (item == editData.unit) {
                    break
                }
            }
            unitField.setSelection(counter)
            productName.setText(editData.name)
            quantityField.setText("${editData.quantity}")
            priceField.setText("${editData.unitPrice}")
            warranty.isChecked = (editData.date.isNotEmpty())
            dateField.setText(editData.date)
        }

        positiveButton(R.string.save) {
            val result = fieldsEmpty(productName.text, quantityField.text, priceField.text)
            if (result) {
                val name = productName.text.toString()
                val quantity = if (quantityField.text.isNotEmpty()) quantityField.text.toString().toInt() else 1
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
                if (editData == null) {
                    dataSource.add(product)
                }else {
                    dataSource.remove(editData)
                    dataSource.add(product)
                }
                //toast("Sikeres hozzáadás")
            } else {
                toast("Hiányzó adat!")
            }
        }
        negativeButton(R.string.cancel)
    }
}