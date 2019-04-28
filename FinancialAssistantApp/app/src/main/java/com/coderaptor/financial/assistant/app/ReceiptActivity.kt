package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.adapters.ProductViewHolder
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_receipt.*
import kotlinx.android.synthetic.main.content_receipt.*

class ReceiptActivity: AppCompatActivity() {
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        addnewproduct.setOnClickListener {
            val intent = Intent(this, AddNewProductActivity::class.java)
            startActivity(intent)
        }

        val dataSource: DataSource<Any> = dataSourceOf(dbHandler.findAllProduct())

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