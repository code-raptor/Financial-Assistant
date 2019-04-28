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
import com.coderaptor.financial.assistant.app.adapters.ReceiptViewHolder
import com.coderaptor.financial.assistant.app.adapters.TransactionViewHolder
import com.coderaptor.financial.assistant.app.core.Receipt
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    private val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val list = mutableListOf<Any>()
        val transactions = dbHandler.findAllTransaction()
        val receipts = dbHandler.findAllReceipt()

        list.addAll(transactions)
        list.addAll(receipts)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        receiptButton.setOnClickListener {
            val intent = Intent(this, IncomeActivity::class.java)
            startActivity(intent)
        }

        repeatButton.setOnClickListener {
            val intent = Intent(this, AddNewRepeatActivity::class.java)
            startActivity(intent)
        }

        onceButton.setOnClickListener {
            val intent = Intent(this, AddNewDreamActivity::class.java)
            startActivity(intent)
        }

        val dataSource: DataSource<Any> = dataSourceOf(list)

        recyclerView_h.setup {

            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    toast("delete $index: ${item}")
                    if (item is Transaction) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
                    } else if (item is Receipt) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_RECEIPT)
                    }
                    true
                }
            }

            withSwipeAction(SwipeLocation.RIGHT) {
                icon(R.drawable.ic_edit_white_24dp)
                text(R.string.edit)
                color(R.color.edit)
                callback { index, item ->
                    toast("edit $index: ${item}")
                    if (item is Transaction) {
                        //edit layout
                    } else if (item is Receipt) {
                        //edit layout
                    }
                    false
                }
            }

            withDataSource(dataSource)

            withItem<Transaction>(R.layout.list_income) {
                onBind(::TransactionViewHolder) { _, item ->
                    // PersonViewHolder is `this` here
                    name.text = item.name
                    date.text = item.date
                    amount.text = "${item.amount}"
                }
                onClick { index ->
                    // item is a `val` in `this` here
                    toast("Clicked $index: ${item.name}")
                }
            }

            withItem<Receipt>(R.layout.list_receipt) {
                onBind(::ReceiptViewHolder) { _, item ->
                    // PersonViewHolder is `this` here
                    name.text = getString(R.string.receipt_string)
                    date.text = item.date
                    amount.text = "${item.amount}"
                }
                onClick { index ->
                    // item is a `val` in `this` here
                    toast("Clicked $index: ${item}")
                }
            }
        }
    }
}

