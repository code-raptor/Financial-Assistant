package me.belakd.smsreadertest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.row_layout.view.*
import java.util.ArrayList

class ListAdapter(private val context: Context, private val list: ArrayList<SmsData>, private val selection: String) : BaseAdapter() {
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false)
        if (selection == MainActivity.UNREAD) {
            view.sms_sender.setTypeface(null, Typeface.BOLD)
            // igy lehet onClick listener-t rakni csak az olvasatlan sms-re
            view.setOnClickListener {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
            }
        }
        view.sms_sender.text = list[position].senderName
        view.sms_message.text = list[position].message
        view.sms_date.text = "${list[position].date} \n"

        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}