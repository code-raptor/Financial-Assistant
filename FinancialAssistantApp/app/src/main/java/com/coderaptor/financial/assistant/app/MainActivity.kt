package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.adapters.TransactionListAdapter
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.core.ProductCategory
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupDatabase()

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

    private fun setupDatabase() {
        val transactionList = arrayListOf(
//            Transaction(-5000, "2019.01.01", "Telefon Számla", "Havonta"),
//            Transaction(1000, "2019.01.01", "Zsebpénz", "Egyszeri"),
            Transaction(1000, "2019.01.01", "Bor"))
//        dbHandler.deleteAll(DatabaseHandler.TABLE_NAME_TRANSACTION)
        dbHandler.inserts(transactionList)

        /*val category = ProductCategory("élelmiszer", 1, "false")
        dbHandler.insert(category)
        dbHandler.findAllProductCategory().forEach {
            Log.i("db", it.toString())
        }*/

        /*val product = Product("alma", "db", 3, 120, 1)
        dbHandler.insert(product)
        dbHandler.findAllProduct().forEach {
            Log.i("db", it.toString())
        }*/



        setUpRecyclerView(dbHandler.findAllTransaction())
    }
}