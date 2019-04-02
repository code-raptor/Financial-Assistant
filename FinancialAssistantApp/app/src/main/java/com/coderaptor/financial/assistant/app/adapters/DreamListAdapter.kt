package com.coderaptor.financial.assistant.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.Dream
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import kotlinx.android.synthetic.main.list_dreams.view.*

class DreamListAdapter(private val dreamList: ArrayList<Dream>) : RecyclerView.Adapter<DreamListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dreamList[position])
    }

    fun addDream(dreams: Dream) {
        dreamList.add(dreams)
        notifyItemInserted(dreamList.size)
    }

    fun removeDream(position: Int, dbHandler: DatabaseHandler) {
        dbHandler.deleteByPosition(dreamList[position].id, DatabaseHandler.TABLE_NAME_DREAM)
        notifyItemRemoved(position)
        dreamList.removeAt(position)
    }

    override fun getItemCount(): Int = dreamList.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_dreams, parent, false)) {

        fun bind(dreams: Dream) = with(itemView) {
            dream_nameField.text = dreams.name
            dream_where.text = dreams.where
            if (dreams.amount > 0) {
                dream_price.setTextColor(ContextCompat.getColor(context, R.color.amount_plus))
            }else {
                dream_price.setTextColor(ContextCompat.getColor(context, R.color.amount_minus))
            }
            dream_price.text = dreams.amount.toString()

            setOnClickListener { view ->
                Toast.makeText(
                    view.context,
                    "click on item: " + dreams.where,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}