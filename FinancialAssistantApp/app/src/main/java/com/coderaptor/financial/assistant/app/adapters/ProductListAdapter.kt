package com.coderaptor.financial.assistant.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.core.Product
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import kotlinx.android.synthetic.main.list_product.view.*

class ProductListAdapter(private val productList: ArrayList<Product>) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    fun addProduct(dreams: Product) {
        productList.add(dreams)
        notifyItemInserted(productList.size)
    }

    fun removeProduct(position: Int, dbHandler: DatabaseHandler) {
        dbHandler.deleteByPosition(productList[position].id, DatabaseHandler.TABLE_NAME_PRODUCT)
        notifyItemRemoved(position)
        productList.removeAt(position)
    }

    override fun getItemCount(): Int = productList.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_product, parent, false)) {

        fun bind(product: Product) = with(itemView) {
            name.text = product.name
            egyseg.text = product.unit
            egysegar.text = product.unitPrice.toString()
            mennyiseg.text = product.quantity.toString()
            price.text = (product.quantity * product.unitPrice).toString()

            setOnClickListener { view ->
                Toast.makeText(
                    view.context,
                    "click on item categoryId: " + product.categoryId,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}