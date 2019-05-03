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
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.adapters.ProductViewHolder
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.*
import com.coderaptor.financial.assistant.app.util.spinner.StringWithId
import com.coderaptor.financial.assistant.app.util.spinner.getCategoryStringWithIdList
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_receipt.*
import kotlinx.android.synthetic.main.content_receipt.*
import kotlinx.android.synthetic.main.content_receipt.dateField
import kotlinx.android.synthetic.main.dialog_add_income.view.*
import kotlinx.android.synthetic.main.dialog_add_product.*

class ReceiptActivity: AppCompatActivity() {
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        val dataSource: DataSource<Any> = dataSourceOf(dbHandler.findAllProduct())

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        dateField.isClickable = true
        dateField.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
        dateField.setOnClickListener {
            openCalendar(it.dateField)
        }

        savefab.setOnClickListener {

            val result = fieldsEmpty(amountField.text)
            if (result) {
                val amount: Int = amountField.text.toString().toInt()
                val date = dateField.text.toString()
                val comment = descript.text.toString()

                //ezzel még munka van
                toast("sikeres hozzáadás")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                toast("Hiányzó adat!")
            }
        }

        val adapter = ArrayAdapter<StringWithId>(this, android.R.layout.simple_spinner_item, getCategoryStringWithIdList(dbHandler))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fab.setOnClickListener {
            MaterialDialog(this).show {
                setTheme(R.style.AppTheme)
                title(R.string.product_string)
                customView(R.layout.dialog_add_product, scrollable = true)

                categoryField.adapter = adapter

                val categoryIdWithWarrantyAndHarmful = dbHandler.categoriesWithWarrantyAndHarmful()
                categoryField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val swt = parent.selectedItem as StringWithId
                        val categoryId = swt.id
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

                val datefield = getCustomView().dateField
                datefield.isClickable = true
                datefield.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
                datefield.setOnClickListener {
                    openCalendar(datefield)
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

                positiveButton(R.string.save) { dialog ->
                    val result = fieldsEmpty(productName.text, quantityField.text, priceField.text)

                    if (result) {
                        val name = productName.text.toString()
                        val quantity = if (quantityField.text.isNotEmpty()) quantityField.text.toString().toInt() else 1
                        val unit = unitField.selectedItem.toString()
                        val price = priceField.text.toString().toInt()
                        val warranty = warranty.isChecked
                        var product = Product(name, unit, quantity, price,categoryId = 45/*categoryId = categoryId*/)
                        if (warranty) {
                            if (dateField.text.isNotEmpty()) {
                                val endDate = dateField.text.toString()
                                product = Product(name, unit, quantity, price, endDate, 45 /*categoryId*/)
                            }
                        }

                        dbHandler.insert(product)
                        dataSource.add(product)
                        toast("Sikeres hozzáadás")
                    } else {
                        toast("Hiányzó adat!")
                    }
                }
                negativeButton(R.string.cancel)
            }
        }

        recyclerView.setup {

            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    toast("delete $index: ${item}")
                    if (item is Product)
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_PRODUCT)
                    true
                }
            }

            withSwipeAction(SwipeLocation.RIGHT) {
                icon(R.drawable.ic_edit_white_24dp)
                text(R.string.edit)
                color(R.color.edit)
                callback { index, item ->
                    toast("edit $index: ${item}")
                    if (item is Product) {
                        //edit layout
                    }
                    false
                }
            }
            withEmptyView(nodata)
            withDataSource(dataSource)

            withItem<Product>(R.layout.list_product) {
                onBind(::ProductViewHolder) { _, item ->
                    name.text = item.name
                    unit.text = item.unit
                    unitPrice.text = "${item.unitPrice}"
                    quantity.text = "${item.quantity}"
                    amount.text  = "${(item.unitPrice * item.quantity)}"
                }
                onClick { index ->
                    toast("Clicked $index: ${item.name}")
                }
            }
        }
    }
}