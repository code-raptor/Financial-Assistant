package com.coderaptor.financial.assistant.app.adapters

import android.view.View
import android.widget.TextView
import com.afollestad.recyclical.ViewHolder
import com.coderaptor.financial.assistant.app.R

class TransactionViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.income_nameField)
    val date: TextView = itemView.findViewById(R.id.income_dateField)
    val amount: TextView = itemView.findViewById(R.id.income_amountField)
}

class ReceiptViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.receipt_nameField)
    val date: TextView = itemView.findViewById(R.id.receipt_dateField)
    val amount: TextView = itemView.findViewById(R.id.receipt_amountField)
}