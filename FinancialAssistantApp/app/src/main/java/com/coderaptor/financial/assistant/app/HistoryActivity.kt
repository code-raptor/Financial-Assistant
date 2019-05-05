package com.coderaptor.financial.assistant.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.view.*

class HistoryActivity : AppCompatActivity() {
    private val dbHandler = DatabaseHandler(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        receiptButton.colorNormal = receiptButton.colorDisabled
        repeatButton.colorNormal = repeatButton.colorDisabled
        onceButton.colorNormal = onceButton.colorDisabled

        val list = ArrayList<Any>()
        val receipts = dbHandler.findAllReceipt("GROUP BY ${DatabaseHandler.RECEIPT_ID}")
        val onceTransactions = dbHandler.findAllTransaction("${DatabaseHandler.FREQUENCY_TRANSACTION} = 'Egyszeri'")
        val repeatTransactions = dbHandler.findAllTransaction("${DatabaseHandler.FREQUENCY_TRANSACTION} != 'Egyszeri'")

        list.addAll(receipts)
        list.addAll(onceTransactions)
        list.addAll(repeatTransactions)

        val dataSource: DataSource<Any> = dataSourceOf(list)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        receiptButton.setOnClickListener {
            Log.i("htr", "list a: ${dataSource.size()}")
            it.receiptButton.onClick(dataSource, receipts)
            Log.i("htr", "list b: ${dataSource.size()}")
        }

        repeatButton.setOnClickListener {
            Log.i("htr", "list a: ${dataSource.size()}")
            it.repeatButton.onClick(dataSource, repeatTransactions)
            Log.i("htr", "list b: ${dataSource.size()}")
        }

        onceButton.setOnClickListener {
            Log.i("htr", "list a: ${dataSource.size()}")
            it.onceButton.onClick(dataSource, onceTransactions)
            Log.i("htr", "list b: ${dataSource.size()}")
        }

        recyclerView.setup {
            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    //toast("delete $index: ${item}")
                    if (item is Transaction) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
                    } else if (item is Receipt) {
                        dbHandler.deleteByReceiptByReceiptId(item.baseID, DatabaseHandler.TABLE_NAME_RECEIPT)
                    }
                    dbHandler.close()
                    true
                }
            }

            withSwipeAction(SwipeLocation.RIGHT) {
                icon(R.drawable.ic_edit_white_24dp)
                text(R.string.edit)
                color(R.color.edit)
                callback { index, item ->
                    //toast("edit $index: ${item}")
                    if (item is Transaction) {
                        //edit layout
                    } else if (item is Receipt) {
                        //edit layout
                    }
                    false
                }
            }
            withEmptyView(nodata)
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
                    //toast("Clicked $index: ${item.name}")
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
                    //toast("Clicked $index: ${item}")
                }
            }
        }

    }

    private fun FloatingActionButton.onClick(dataSource: DataSource<Any>, typeList: List<Any>) {
        if(colorNormal != colorDisabled){
            colorNormal = colorDisabled
            typeList.forEach { dataSource.add(it) }
        }
        else{
            colorNormal = resources.getColor(R.color.closed_fab, null)
            typeList.forEach { dataSource.remove(it) }
        }
    }

    override fun onStop() {
        super.onStop()
        dbHandler.close()
    }
}

