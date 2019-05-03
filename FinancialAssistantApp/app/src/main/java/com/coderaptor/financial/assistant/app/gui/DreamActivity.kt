package com.coderaptor.financial.assistant.app.gui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.swipe.SwipeLocation
import com.afollestad.recyclical.swipe.withSwipeAction
import com.afollestad.recyclical.withItem
import com.coderaptor.financial.assistant.app.MainActivity
import com.coderaptor.financial.assistant.app.R
import com.coderaptor.financial.assistant.app.adapters.DreamViewHolder
import com.coderaptor.financial.assistant.app.core.Dream
import com.coderaptor.financial.assistant.app.data.DatabaseHandler
import com.coderaptor.financial.assistant.app.util.toast
import kotlinx.android.synthetic.main.activity_dreams.*
import kotlinx.android.synthetic.main.dialog_add_dream.*
import kotlinx.android.synthetic.main.dialog_add_dream.view.*

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

        savefab.setOnClickListener{
            MaterialDialog(this).show {
                setTheme(R.style.AppTheme)
                title(R.string.newDream)
                customView(R.layout.dialog_add_dream, scrollable = true)

                positiveButton(R.string.save) { dialog ->
                    val result = fieldsEmpty(nameField.text, amountField.text, whereField.text)

                    if(result) {
                        val amount = dialog.getCustomView().amountField.text.toString().toInt()
                        val name = dialog.getCustomView().nameField.text.toString()
                        val where = dialog.getCustomView().whereField.text.toString()

                        val dream = Dream(name, amount, where)

                        dbHandler.insert(dream)
                        toast("Sikeres hozzáadás!")
                    }
                    else{
                        toast("Hiányzó adat!")
                    }
                }
                negativeButton(R.string.cancel)
            }
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
            withEmptyView(nodata)
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

    private fun fieldsEmpty(vararg fields: Editable):Boolean{
        for (data in fields){
            if(data.isEmpty()){
                return false
            }
        }
        return true
    }
}