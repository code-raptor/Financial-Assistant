package com.coderaptor.financial.assistant.app

import android.annotation.SuppressLint
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
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    private val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var list = mutableListOf<Any>()
        val transactions = dbHandler.findAllTransaction()
        val receipts = dbHandler.findAllReceipt()

        list.addAll(transactions)
        list.addAll(receipts)

        var dataSource: DataSource<Any> = dataSourceOf(list)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        receiptButton.setOnClickListener {
            list = onClick(receiptButton, list, receipts)
            dataSource = dataSourceOf(list)
            recyclerViewSetup(dataSource)
        }

        repeatButton.setOnClickListener {
            list = onClick(repeatButton, list, transactions)
            dataSource = dataSourceOf(list)
            recyclerViewSetup(dataSource)
        }

        onceButton.setOnClickListener {
            list = onClick(onceButton, list, transactions)
            dataSource = dataSourceOf(list)
            recyclerViewSetup(dataSource)
        }

        recyclerViewSetup(dataSource)
    }

    private fun onClick(fab : FloatingActionButton, historyList : MutableList<Any>, list: List<Any>): MutableList<Any> {
        if(fab.colorNormal == fab.colorDisabled){
            fab.colorNormal = resources.getColor(R.color.closed_fab)
            historyList.addAll(list)
        }
        else{
            fab.colorNormal = fab.colorDisabled
            historyList.removeAll(list)
        }
        return historyList
    }

    @SuppressLint("SetTextI18n")
    private fun recyclerViewSetup(dataSource : DataSource<Any>){

        recyclerView.setup {

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
                    name.text = item.name

                    if (item.hasFrequency()) {
                        date.text = item.date + getString(R.string.tab) + item.frequency
                    }else {
                        date.text = item.date
                    }

                    if (item.amount > 0) {
                        amount.setTextColor(resources.getColor(R.color.amount_plus, null))
                        amount.text = "+${item.amount}"
                    }else {
                        amount.setTextColor(resources.getColor(R.color.amount_minus, null))
                        amount.text = item.amount.toString()
                    }
                }
                onClick { index ->
                    toast("Clicked $index: ${item.name}")
                }
            }

            withItem<Receipt>(R.layout.list_receipt) {
                onBind(::ReceiptViewHolder) { _, item ->
                    name.text = getString(R.string.receipt_string)
                    date.text = item.date
                    amount.setTextColor(resources.getColor(R.color.amount_minus, null))
                    amount.text = "-${item.amount}"
                }
                onClick { index ->
                    toast("Clicked $index: ${item}")
                }
            }
        }
    }
}

