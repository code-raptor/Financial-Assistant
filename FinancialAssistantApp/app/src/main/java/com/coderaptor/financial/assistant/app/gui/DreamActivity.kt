package com.coderaptor.financial.assistant.app.gui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.AddNewDreamActivity
import com.coderaptor.financial.assistant.app.MainActivity
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.adapters.DreamViewHolder
import com.coderaptor.financial.assistant.app.core.Dream
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_dreams.*

class DreamActivity : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dreams)

        setupDatabase()

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        addnewDream.setOnClickListener{
            val intent = Intent(this, AddNewDreamActivity::class.java)
            startActivity(intent)
        }

        val dataSource: DataSource<Any> = dataSourceOf(dbHandler.findAllDream())

        recyclerView.setup {

            withSwipeAction(SwipeLocation.LEFT) {
                icon(R.drawable.ic_delete_white_24dp)
                text(R.string.delete)
                color(R.color.delete)
                callback { index, item ->
                    toast("delete $index: ${item}")
                    if (item is Dream)
                        dbHandler.deleteByPosition(item.id, DatabaseHandler.TABLE_NAME_DREAM)
                    true
                }
            }

            withSwipeAction(SwipeLocation.RIGHT) {
                icon(R.drawable.ic_edit_white_24dp)
                text(R.string.edit)
                color(R.color.edit)
                callback { index, item ->
                    toast("edit $index: ${item}")
                    if (item is Dream) {
                        //edit layout
                    }
                    false
                }
            }

            withDataSource(dataSource)

            withItem<Dream>(R.layout.list_dreams) {
                onBind(::DreamViewHolder) { _, item ->
                    name.text = item.name
                    where.text = item.where
                    amount.text = "${item.amount}"
                }
                onClick { index ->
                    toast("Clicked $index: ${item.name}")
                }
            }
        }
    }

    private fun setupDatabase() {
        val dreamsList = arrayListOf(
            Dream("Samsung HD Tv", 55000, "Media Markt"),
            Dream("Fűnyíró", 100000, "OBI")
        )
        dbHandler.inserts(dreamsList)
    }
}