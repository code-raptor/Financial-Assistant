package com.coderaptor.financial.assistant.app.gui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.MainActivity
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.adapters.TransactionViewHolder
import com.coderaptor.financial.assistant.app.core.Transaction
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.gui.dialogs.openRepeatDialog
import kotlinx.android.synthetic.main.activity_repeats.*

class RepeatActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeats)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val list = dbHandler.findAllTransaction("${DatabaseHandler.FREQUENCY_TRANSACTION} != 'Egyszeri'")
        val dataSource: DataSource<Any> = dataSourceTypedOf(list)

        savefab.setOnClickListener{
            openRepeatDialog(dataSource, dbHandler)
        }

        recyclerView.setup {

            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    //toast("delete $index: ${item}")
                    if (item is Transaction) {
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_TRANSACTION)
                    }
                    true
                }
            }

            withSwipeAction(SwipeLocation.RIGHT) {
                icon(R.drawable.ic_edit_white_24dp)
                text(R.string.edit)
                color(R.color.edit)
                callback { index, item ->
                    if (item is Transaction) {
                        openRepeatDialog(dataSource, dbHandler, item)
                    }
                    false
                }
            }
            withEmptyView(nodata)
            withDataSource(dataSource)
            withItem<Transaction>(R.layout.list_income) {
                onBind(::TransactionViewHolder) { _, item ->
                    name.text = item.name

                    if (item.hasFrequency()) {
                        date.text = item.date + getString(R.string.tab) + item.frequency
                    }else {
                        date.text = item.date
                    }

                    if (item.amount > 0) {
                        amount.setTextColor(resources.getColor(R.color.amount_plus, null))
                        amount.text = "+${item.amount}"
                    }else {
                        amount.setTextColor(resources.getColor(R.color.amount_minus, null))
                        amount.text = item.amount.toString()
                    }
                }
                onClick { index ->
                    //toast("Clicked $index: ${item.name}")
                }
            }
        }
    }
}

