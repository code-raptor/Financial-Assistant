package com.coderaptor.financial.assistant.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.ShoppingList
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import kotlinx.android.synthetic.main.list_shoppinglist.view.*

class ShoppingListAdapter (private val shoppingList: ArrayList<ShoppingList>) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shoppingList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListAdapter.ViewHolder {
        return ShoppingListAdapter.ViewHolder(parent)
    }

    fun removeShopElement(position: Int, dbHandler: DatabaseHandler){
        dbHandler.deleteByPosition(shoppingList[position].id, DatabaseHandler.TABLE_NAME_DREAM)
        notifyItemRemoved(position)
        shoppingList.removeAt(position)
    }

    override fun getItemCount(): Int = shoppingList.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_shoppinglist, parent, false)) {

        fun bind(shoppingElements: ShoppingList) = with(itemView) {

            listElement.text = shoppingElements.name
            unitField.text = shoppingElements.quantity.toString()
            mennyiseg.text = shoppingElements.unit

            setOnClickListener { view ->
                Toast.makeText(
                    view.context,
                    "click on item: " + shoppingElements.name + ", " + shoppingElements.quantity.toString() + ", " + shoppingElements.unit,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}