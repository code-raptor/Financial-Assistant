package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.coderaptor.financial.assistant.app.adapters.ProductListAdapter
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import com.coderaptor.financial.assistant.app.util.formatDate
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_receipt.*
import kotlinx.android.synthetic.main.content_receipt.*

class ReceiptActivity: AppCompatActivity() {
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        setUpRecyclerView(dbHandler.findAllProduct())

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        addnewproduct.setOnClickListener {
            val intent = Intent(this, AddNewProductActivity::class.java)
            startActivity(intent)
        }

        dateField.isClickable = true
        dateField.text = Editable.Factory.getInstance().newEditable(java.util.Calendar.getInstance().formatDate())
        dateField.setOnClickListener {
            dateClick(this)
        }

        fab.setOnClickListener {
            val result = fieldsEmpty(amountField.text)

            if (result){

                var amount: Int = amountField.text.toString().toInt()
                    //TODO ez nem is tudom mit csinál -- NYUGTA
                toast("sikeres hozzáadás")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                toast("Hiányzó adat!")
            }
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as ProductListAdapter
                adapter.removeProduct(viewHolder.adapterPosition, dbHandler)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setUpRecyclerView(findAllProduct: MutableList<Product>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val transactionListAdapter = ProductListAdapter(findAllProduct as ArrayList<Product>)
        recyclerView.hasFixedSize()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionListAdapter
    }

    fun dateClick(context: ReceiptActivity) {
        MaterialDialog(this).show {
            setTheme(R.style.AppTheme)
            datePicker { _, innerDate ->
                context.dateField.text = Editable.Factory.getInstance().newEditable(innerDate.formatDate())
            }
        }
    }

    fun fieldsEmpty(vararg fields: Editable):Boolean{
        for (data in fields){
            if(data.isEmpty()){
                return false
            }
        }
        return true
    }
}