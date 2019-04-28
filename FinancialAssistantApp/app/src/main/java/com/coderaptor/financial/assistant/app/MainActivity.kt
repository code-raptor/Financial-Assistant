package com.coderaptor.financial.assistant.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.adapters.TransactionAndReceiptAdapter
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.features.limit.checkDayChanged
import com.coderaptor.financial.assistant.app.features.oneweek.getOneWeekData
import com.coderaptor.financial.assistant.app.features.sms.askPermission
import com.coderaptor.financial.assistant.app.features.sms.getSmsMessages
import com.coderaptor.financial.assistant.app.gui.DreamActivity
import com.coderaptor.financial.assistant.app.gui.RepeatActivity
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import com.coderaptor.financial.assistant.app.util.SharedPreference
import com.coderaptor.financial.assistant.app.util.formatDate
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(){

    val dbHandler = DatabaseHandler(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (!SharedPreference.firstRun) {
            SharedPreference.firstRun = true
            SharedPreference.currentDate = Calendar.getInstance().formatDate()
        }else {
            Log.i("first", "Nem először value: ${SharedPreference.firstRun}")
                SharedPreference.currentDate = Calendar.getInstance().formatDate()
        }

        setupSms(dbHandler.findMaxSMS())
        checkDayChanged(dbHandler)
        setUpRecyclerView(getOneWeekData(dbHandler))
        dbHandler.insertTestdata()

        addNewButton.setOnClickListener {
            val intent = Intent(this, IncomeActivity::class.java)
            startActivity(intent)
        }

        repeatButton.setOnClickListener {
            val intent = Intent(this, RepeatActivity::class.java)
            startActivity(intent)
        }

        dreamButton.setOnClickListener {
            val intent = Intent(this, DreamActivity::class.java)
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

        shoppingListButton.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as TransactionAndReceiptAdapter
                adapter.remove(viewHolder.adapterPosition, dbHandler)

                if(adapter.itemCount == 0){
                    nodata.visibility = View.VISIBLE
                }
                else{
                    nodata.visibility = View.INVISIBLE
                }
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setUpRecyclerView(oneWeekList: MutableList<Any>) {
        val tAndRAdapter = TransactionAndReceiptAdapter(oneWeekList as ArrayList<Any>)
        recyclerView.hasFixedSize()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tAndRAdapter

        if(oneWeekList.size == 0){
            nodata.visibility = View.VISIBLE
        }
        else{
            nodata.visibility = View.INVISIBLE
        }
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