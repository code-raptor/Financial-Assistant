package com.coderaptor.financial.assistant.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.core.Receipt
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import kotlinx.android.synthetic.main.list_income.view.*

class TransactionAndReceiptAdapter(private val list: ArrayList<Any>) : RecyclerView.Adapter<TransactionAndReceiptAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addTransaction(transaction: Transaction) {
        list.add(transaction)
        notifyItemInserted(list.size)
    }

    fun remove(position: Int, dbHandler: DatabaseHandler) {
        if (list[position] is Transaction) {
            val transaction = list[position] as Transaction
            dbHandler.deleteByPosition(transaction.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
        }else if (list[position] is Receipt) {
            val receipt = list[position] as Receipt
            dbHandler.deleteByPosition(receipt.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
        }
        notifyItemRemoved(position)
        list.removeAt(position)
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(com.coderaptor.financial.assistant.app.R.layout.list_income, parent, false)) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Any) = with(itemView) {
            if (item is Transaction) {
                val transaction = item

                income_nameField.text = transaction.name
                if (transaction.hasFrequency()) {
                    income_dateField.text = "${transaction.date}\t${transaction.frequency}"
                } else {
                    income_dateField.text = transaction.date
                }

                if (transaction.amount > 0) {
                    income_amountField.setTextColor(
                        ContextCompat.getColor(context, com.coderaptor.financial.assistant.app.R.color.amount_plus)
                    )
                    income_amountField.text = "+${transaction.amount}"
                } else {
                    income_amountField.setTextColor(
                        ContextCompat.getColor(context, com.coderaptor.financial.assistant.app.R.color.amount_minus)
                    )
                    income_amountField.text = transaction.amount.toString()
                }
                setOnClickListener { view ->
                    Toast.makeText(
                        view.context,
                        "click on item: " + transaction.name,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else if (item is Receipt) {
                val receipt = item

                income_dateField.text = receipt.date
                income_amountField.setTextColor(
                    ContextCompat.getColor(context, com.coderaptor.financial.assistant.app.R.color.amount_minus)
                )
                income_amountField. text = receipt.amount.toString()
                income_nameField.text = ""
            }
        }
    }

}