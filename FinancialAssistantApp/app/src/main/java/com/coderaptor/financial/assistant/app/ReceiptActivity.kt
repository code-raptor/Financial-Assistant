package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.adapters.ProductViewHolder
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.core.Receipt
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

        dateField.isClickable = true
        dateField.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())

        val dataSource: DataSource<Product> = emptyDataSourceTyped()
        var bundleReceipt: Receipt? = null
        val bundle = intent.extras
        val productList = ArrayList<Product>()

        if (bundle != null) {
            val baseId = bundle.getLong("id")
            var productIds = "("
            val receipts = dbHandler.findAllReceipt("WHERE ${DatabaseHandler.RECEIPT_ID} = $baseId")
            receipts.forEach {
                productIds += "${it.productId}, "
            }

            productIds = productIds.substring(0, productIds.length-2)
            productIds += ")"
            dbHandler.findAllProduct("${DatabaseHandler.BASE_ID} IN $productIds").forEach {
                dataSource.add(it)
                productList.add(it)
            }

            bundleReceipt = receipts.first()
            dateField.text = Editable.Factory.getInstance().newEditable(bundleReceipt.date)
            amountField.text =  Editable.Factory.getInstance().newEditable(bundleReceipt.amount.toString())
            descriptField.text = Editable.Factory.getInstance().newEditable(bundleReceipt.comment)
        }

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        dateField.setOnClickListener {
            openCalendar(it.dateField)
        }

        savefab.setOnClickListener {
            val result = fieldsEmpty(amountField.text, dateField.text)
            if (result) {
                val amount: Int = amountField.text.toString().toInt()
                val date = dateField.text.toString()
                val comment = descriptField.text.toString()
                val idTime = java.util.Calendar.getInstance().timeInMillis
                val receiptID = amount + idTime
                dataSource.forEach {
                    if (bundle == null) {
                    val id = dbHandler.insert(it)
                    var receipt = Receipt(receiptID, date, amount, productId = id)
                    if (comment.isNotEmpty()) {
                        receipt = Receipt(receiptID, date, amount, comment, id)
                    }
                        dbHandler.insert(receipt)
                    }
                }
                if(bundle != null) {
                    productList.forEach {
                        var nextUpdatedId = bundleReceipt!!.id
                        var receipt = Receipt(nextUpdatedId, bundleReceipt.baseID, date, amount, productId = it.id)
                        if (comment.isNotEmpty()) {
                            receipt = Receipt(nextUpdatedId, bundleReceipt.baseID, date, amount, comment, it.id)
                        }
                         dbHandler.updateReceipt(receipt)
                        nextUpdatedId += 1
                        Log.i("receipt", receipt.toString())
                    }
                    if (productList.size < dataSource.size()) {
                        dataSource.forEach {
                            if (!productList.contains(it)) {
                                val id = dbHandler.insert(it)
                                var receipt = Receipt(bundleReceipt!!.baseID, date, amount, productId = id)
                                if (comment.isNotEmpty()) {
                                    receipt = Receipt(bundleReceipt.baseID, date, amount, comment, id)
                                }
                                dbHandler.insert(receipt)
                            }
                        }
                    }
                }
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
                title(R.string.newDream)
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