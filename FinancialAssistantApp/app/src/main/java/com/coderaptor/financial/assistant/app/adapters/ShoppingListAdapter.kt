package com.coderaptor.financial.assistant.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.ShoppingList
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import kotlinx.android.synthetic.main.list_shoppinglist.view.*

class ShoppingListAdapter (private val shoppingList: ArrayList<ShoppingList>, val clickListener: (ShoppingList) -> Unit) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shoppingList[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListAdapter.ViewHolder {
        return ShoppingListAdapter.ViewHolder(parent)
    }

    fun removeShopElement(position: Int, dbHandler: DatabaseHandler){
        dbHandler.deleteByPosition(shoppingList[position].id, DatabaseHandler.TABLE_NAME_SHOPPING)
        notifyItemRemoved(position)
        shoppingList.removeAt(position)
    }

    fun addShopElement(toPosition: Int, item: ShoppingList, dbHandler: DatabaseHandler) {
        dbHandler.insert(item)
        notifyItemInserted(toPosition)
    }

    fun moveSingleItem(position: Int, dbHandler: DatabaseHandler) {
        val toPosition = shoppingList.size-1
        val item = shoppingList[position]
        removeShopElement(position, dbHandler)
        addShopElement(toPosition, item, dbHandler)
        shoppingList.add(toPosition, item)

        notifyItemMoved(position, toPosition)
    }

    override fun getItemCount(): Int = shoppingList.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_shoppinglist, parent, false)) {

        fun bind(shoppingElement: ShoppingList, clickListener: (ShoppingList) -> Unit) = with(itemView) {
            listElement.text = shoppingElement.name
            unitField.text = shoppingElement.quantity.toString()
            mennyiseg.text = shoppingElement.unit
            if (shoppingElement.isBought) {
                listElement.setCheckMarkDrawable(R.drawable.checked_circle_black)
            }else {
                listElement.setCheckMarkDrawable(R.drawable.unchecked_circle_black)
            }
            setOnClickListener {
                if (!shoppingElement.isBought) {
                    listElement.setCheckMarkDrawable(R.drawable.checked_circle_black)
                }else {
                    listElement.setCheckMarkDrawable(R.drawable.unchecked_circle_black)
                }
                clickListener(shoppingElement)
            }
        }
    }
}