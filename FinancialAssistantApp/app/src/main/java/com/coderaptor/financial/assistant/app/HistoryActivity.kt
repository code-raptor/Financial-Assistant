package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.adapters.TransactionAndReceiptAdapter
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity: AppCompatActivity() {
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

        setUpRecyclerView(list)

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

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as TransactionAndReceiptAdapter
                adapter.remove(viewHolder.adapterPosition, dbHandler)
            }

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setUpRecyclerView(allList: MutableList<Any>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val transactionAndReceiptAdapter = TransactionAndReceiptAdapter(allList as ArrayList<Any>)
        recyclerView.hasFixedSize()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionAndReceiptAdapter
    }

}

