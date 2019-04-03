package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.adapters.TransactionListAdapter
import com.coderaptor.financial.assistant.app.core.ProductProperty
import com.coderaptor.financial.assistant.app.core.Receipt
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupDatabase()

        val firstFab: FloatingActionButton = findViewById(R.id.addNewButton)
        firstFab.setOnClickListener {
            Log.i("click", "add new clicked")
            val intent = Intent(this, IncomeActivity::class.java)
            startActivity(intent)
        }
        val secondFab: FloatingActionButton = findViewById(R.id.repeatButton)
        secondFab.setOnClickListener {
            val intent = Intent(this, AddNewRepeatActivity::class.java)
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.dreamButton).setOnClickListener {
            val intent = Intent(this, AddNewDreamActivity::class.java)
            startActivity(intent)
        }

        val settings: ImageButton = findViewById(R.id.settings)
        Log.i("click", "gomb")
        settings.setOnClickListener {
            Log.i("click", "setting")
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
            Transaction(-5000, "2019.01.01", "Telefon Számla", "Havonta"),
            Transaction(1000, "2019.01.01", "Zsebpénz", "Egyszeri"),
            Transaction(1000, "2019.01.01", "Bor", "Hetente"))
        dbHandler.deleteAll(DatabaseHandler.TABLE_NAME_TRANSACTION)
        dbHandler.inserts(transactionList)

        dbHandler.deleteAll(DatabaseHandler.TABLE_NAME_PRODUCT_PROPERTY)
        var property = ProductProperty("jotallas", "Boolean")
        dbHandler.insert(property)
        property = ProductProperty("karos", "Boolean")
        dbHandler.insert(property)
        dbHandler.findAllProductProperty().forEach {
            Log.i("db", it.toString())
        }
        dbHandler.deleteAll(DatabaseHandler.TABLE_NAME_RECEIPT)
        var receipt = Receipt(1,"2019.01.01", 13000, 1)
        dbHandler.insert(receipt)
        receipt = Receipt(2,"2019.10.01", 5000, 2)
        dbHandler.insert(receipt)
        dbHandler.findAllReceipt().forEach {
            Log.i("db", it.toString())
        }
        setUpRecyclerView(dbHandler.findAllTransaction())
    }
}
