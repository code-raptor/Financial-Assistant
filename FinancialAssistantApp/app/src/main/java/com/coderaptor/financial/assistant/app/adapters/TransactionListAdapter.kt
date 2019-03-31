package com.coderaptor.financial.assistant.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import kotlinx.android.synthetic.main.list_income.view.*


class TransactionListAdapter(private val transactionList: ArrayList<Transaction>) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactionList[position])
    }

    fun addTransaction(transaction: Transaction) {
        transactionList.add(transaction)
        notifyItemInserted(transactionList.size)
    }

    fun removeTransaction(position: Int, dbHandler: DatabaseHandler) {
        dbHandler.deleteByPosition(transactionList[position].id, "trans")
        notifyItemRemoved(position)
        transactionList.removeAt(position)
    }

    override fun getItemCount(): Int = transactionList.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_income, parent, false)) {

        fun bind(transaction: Transaction) = with(itemView) {
            income_nameField.text = transaction.category
            income_dateField.text = transaction.date
            if (transaction.amount > 0) {
                income_amountField.setTextColor(getColor(context, R.color.amount_plus))
            }else {
                income_amountField.setTextColor(getColor(context, R.color.amount_minus))
            }
            income_amountField.text = transaction.amount.toString()

            setOnClickListener { view ->
                Toast.makeText(
                    view.context,
                    "click on item: " + transaction.category,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}