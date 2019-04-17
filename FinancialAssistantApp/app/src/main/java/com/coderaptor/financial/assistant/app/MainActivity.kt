package com.coderaptor.financial.assistant.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.adapters.TransactionListAdapter
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.features.limit.checkDayChanged
import com.coderaptor.financial.assistant.app.features.sms.askPermission
import com.coderaptor.financial.assistant.app.features.sms.getSmsMessages
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    val dbHandler = DatabaseHandler(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupSms(dbHandler.findMaxSMS())
        checkDayChanged(dbHandler)
        setUpRecyclerView(dbHandler.findAllTransaction())
        dbHandler.insertTestdata()

        addNewButton.setOnClickListener {
            val intent = Intent(this, IncomeActivity::class.java)
            startActivity(intent)
        }

        repeatButton.setOnClickListener {
            val intent = Intent(this, AddNewRepeatActivity::class.java)
            startActivity(intent)
        }

        dreamButton.setOnClickListener {
            val intent = Intent(this, AddNewDreamActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        history.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        receiptButton.setOnClickListener {
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as TransactionListAdapter
                adapter.removeTransaction(viewHolder.adapterPosition, dbHandler)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setUpRecyclerView(findAllTransaction: MutableList<Transaction>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val transactionListAdapter = TransactionListAdapter(findAllTransaction as ArrayList<Transaction>)
        recyclerView.hasFixedSize()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionListAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setupSms(findMaxSMS: Long) {
        val permissionGranted = askPermission(this, this)
        if(permissionGranted) {
            val idAndAmount = getSmsMessages(this, findMaxSMS, dbHandler)
            if (idAndAmount.first != (-1).toLong() && idAndAmount.second != -1) {
                dbHandler.insertSms(idAndAmount)
                editText2.setText("${dbHandler.getSmsAmount()} ft")
            }
        }
    }
}