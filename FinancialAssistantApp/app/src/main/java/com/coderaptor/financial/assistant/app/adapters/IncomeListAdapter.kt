package com.coderaptor.financial.assistant.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.Income
import kotlinx.android.synthetic.main.list_income.view.*


class IncomeListAdapter(private val incomeList: ArrayList<Income>) : RecyclerView.Adapter<IncomeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(incomeList[position])
    }

    fun addIncome(income: Income) {
        incomeList.add(income)
        notifyItemInserted(incomeList.size)
    }

    fun removeIncome(position: Int) {
        incomeList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = incomeList.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_income, parent, false)) {

        fun bind(income: Income) = with(itemView) {
            income_nameField.text = income.category
            income_dateField.text = income.date
            if (income.amount > 0) {
                income_amountField.setTextColor(getColor(context, R.color.amount_plus))
            }else {
                income_amountField.setTextColor(getColor(context, R.color.amount_minus))
            }
            income_amountField.text = income.amount.toString()

            setOnClickListener { view ->
                Toast.makeText(
                    view.context,
                    "click on item: " + income.category,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}