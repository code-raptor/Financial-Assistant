package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.coderaptor.financial.assistant.app.adapters.ShoppingListAdapter
import com.coderaptor.financial.assistant.app.core.ShoppingList
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.SwipeToDeleteCallback
import com.coderaptor.financial.assistant.app.util.SharedPreference
import com.coderaptor.financial.assistant.app.util.spinner.StringWithId
import com.coderaptor.financial.assistant.app.util.spinner.getCategoryStringWithIdList
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_shoppinglist.*
import kotlinx.android.synthetic.main.dialog_add_shopping.view.*
import java.util.*

class ShoppingListActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoppinglist)

        setUpRecyclerView(dbHandler.findAllShopping())
        val categoryAdapter = setupDialogCategorySpinner(dbHandler)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener {
            MaterialDialog(this).show {
                setTheme(R.style.AppTheme)
                title(R.string.product_string)
                customView(R.layout.dialog_add_shopping, scrollable = true)
                getCustomView().categoryField.adapter = categoryAdapter
                positiveButton(R.string.income_button) { dialog ->
                    val name = dialog.getCustomView().product_name.text.toString()
                    val quantity = dialog.getCustomView().product_quantity.text.toString().toInt()
                    val unit = dialog.getCustomView().unitField.selectedItem.toString()
                    val category = dialog.getCustomView().categoryField.selectedItem.toString()

                    dbHandler.insert(ShoppingList(name, quantity, unit))
                    toast("name: $name, quantity: $quantity, egység: $unit, category: $category")
                    setUpRecyclerView(dbHandler.findAllShopping())
                }
                negativeButton(android.R.string.cancel)
            }
        }

        send.setOnClickListener {}

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as ShoppingListAdapter
                adapter.removeShopElement(viewHolder.adapterPosition, dbHandler)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupDialogCategorySpinner(dbHandler: DatabaseHandler): ArrayAdapter<StringWithId> {
        val adapter = ArrayAdapter<StringWithId>(this, android.R.layout.simple_spinner_item, getCategoryStringWithIdList(dbHandler, SharedPreference.saving))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return adapter
    }

    private fun setUpRecyclerView(findAllTransaction: MutableList<ShoppingList>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val shoppingListAdapter = ShoppingListAdapter(findAllTransaction as ArrayList<ShoppingList>)
        recyclerView.hasFixedSize()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = shoppingListAdapter
    }
}