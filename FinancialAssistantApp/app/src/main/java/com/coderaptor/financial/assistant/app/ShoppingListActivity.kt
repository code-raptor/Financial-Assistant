package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_shoppinglist.*
import kotlinx.android.synthetic.main.dialog_add_shopping.*
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

        savefab.setOnClickListener {
            MaterialDialog(this).show {
                setTheme(R.style.AppTheme)
                title(R.string.product_string)
                customView(R.layout.dialog_add_shopping, scrollable = true)
                getCustomView().categoryField.adapter = categoryAdapter

                getCustomView().categoryField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val swt = parent.selectedItem as StringWithId
                        val categoryId = swt.id
                        if (SharedPreference.shoppingMonitor) {
                            if (dbHandler.isHarmfulCategory(categoryId)) {
                                Snackbar.make(getCustomView(), "Káros Termék", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show()
                            }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }



                    positiveButton(R.string.save) { dialog ->
                        val result = fieldsEmpty(product_name.text, product_quantity.text)
                        
                        if(result) {
                            val name = dialog.getCustomView().product_name.text.toString()
                            val quantity = dialog.getCustomView().product_quantity.text.toString().toInt()
                            val unit = dialog.getCustomView().unitField.selectedItem.toString()

                            dbHandler.insert(ShoppingList(name, quantity, unit))
                            setUpRecyclerView(dbHandler.findAllShopping())

                        }else{
                                toast("Hiányzó adat!")
                        }

                }
                negativeButton(R.string.cancel)
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

    private fun shoppingElementClicked(element : ShoppingList) {
        element.isBought = !element.isBought
        dbHandler.changeShoppingBought(element)
    }

    private fun setupDialogCategorySpinner(dbHandler: DatabaseHandler): ArrayAdapter<StringWithId> {
        val adapter = ArrayAdapter<StringWithId>(this, android.R.layout.simple_spinner_item, getCategoryStringWithIdList(dbHandler, SharedPreference.saving))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return adapter
    }

    private fun setUpRecyclerView(findAllShopping: MutableList<ShoppingList>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val shoppingListAdapter = ShoppingListAdapter(findAllShopping as ArrayList<ShoppingList>) { element: ShoppingList ->shoppingElementClicked(element)}
        recyclerView.hasFixedSize()
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = shoppingListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHandler.close()
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