package com.coderaptor.financial.assistant.app.adapters

import android.view.View
import android.widget.CheckedTextView
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

class DreamViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.dream_nameField)
    val where: TextView = itemView.findViewById(R.id.dream_where)
    val amount: TextView = itemView.findViewById(R.id.dream_price)
}

class ProductViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.name)
    val unit: TextView = itemView.findViewById(R.id.egyseg)
    val unitPrice: TextView = itemView.findViewById(R.id.egysegar)
    val quantity: TextView = itemView.findViewById(R.id.mennyiseg)
    val amount: TextView = itemView.findViewById(R.id.price)
}

class ShoppingViewHolder(itemView: View) : ViewHolder(itemView) {
    val listElement: CheckedTextView = itemView.findViewById(R.id.listElement)
    val unit: TextView = itemView.findViewById(R.id.unitField)
    val quantity: TextView = itemView.findViewById(R.id.mennyiseg)
}