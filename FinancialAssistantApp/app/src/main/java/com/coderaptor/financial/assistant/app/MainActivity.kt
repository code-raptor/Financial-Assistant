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
import com.coderaptor.financial.assistant.app.adapters.IncomeListAdapter
import com.coderaptor.financial.assistant.app.core.Income
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setUpRecyclerView()

        val firstFab: FloatingActionButton = findViewById(R.id.addNewButton)
        firstFab.setOnClickListener {
            Log.i("click", "addnew clicked")
            val intent = Intent(this, IncomeActivity::class.java)
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
                val adapter = recyclerView.adapter as IncomeListAdapter
                adapter.removeIncome(viewHolder.adapterPosition)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                TODO("not implemented")
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    fun setUpRecyclerView() {
        val incomeList = arrayListOf(
            Income(1, -5000, "2019.01.01", "Telefon Számla", "Havonta"),
            Income(2, 1000, "2019.01.01", "Zsebpénz", "Hetente"),
            Income(3, 2000, "2019.01.01", "Spotify", "Évente"))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val incomeListAdapter = IncomeListAdapter(incomeList)
        recyclerView.hasFixedSize()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = incomeListAdapter
    }
}
