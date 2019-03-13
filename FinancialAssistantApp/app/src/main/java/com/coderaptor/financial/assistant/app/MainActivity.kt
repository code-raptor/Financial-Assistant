package com.coderaptor.financial.assistant.app

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val firstFab: FloatingActionButton = findViewById(R.id.addNewButton)
        firstFab.setOnClickListener {
            Log.i("fab", "addnew clicked")
        }

        val settings: ImageButton = findViewById(R.id.settings)
        Log.i("click", "gomb")
        settings.setOnClickListener {
            Log.i("click", "setting")
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        val listView : SwipeMenuListView = findViewById(R.id.listView)

        val list : MutableList<String> = ArrayList()

        list.add("asd")
        list.add("fasd")
        list.add("asdsd")
        list.add("asddf")
        list.add("asdwqe")

        val adapter: ArrayAdapter<Any> = ArrayAdapter(this, android.R.layout.simple_list_item_1, list as List<Any>)
        listView.adapter = adapter

        val creator = SwipeMenuCreator { menu ->
            val openItem = SwipeMenuItem(
                applicationContext
            )
            openItem.background = ColorDrawable(
                Color.rgb(
                    0xC9,
                    0xC9,
                    0xCE
                )
            )
            openItem.width = (90)
            openItem.setIcon(android.R.drawable.ic_menu_edit)
            openItem.titleSize = 18
            openItem.titleColor = Color.WHITE
            menu.addMenuItem(openItem)

            val deleteItem = SwipeMenuItem(applicationContext)
            deleteItem.background = ColorDrawable(
                Color.rgb(
                    0xF9,
                    0x3F,
                    0x25
                )
            )
            deleteItem.width = (90)
            deleteItem.setIcon(android.R.drawable.ic_delete)
            menu.addMenuItem(deleteItem)
        }
        listView.setMenuCreator(creator)

        listView.setOnMenuItemClickListener(object : SwipeMenuListView.OnMenuItemClickListener {
            override fun onMenuItemClick(position: Int, menu: SwipeMenu, index: Int): Boolean {
                when (index) {
                    0 -> {
                        Log.d("click", "onMenuItemClick: clicked item ->$index")
                    }
                    1 -> {
                        Log.d("click", "onMenuItemClick: clicked item ->$index")
                    }
                }
                // false : close the menu; true : not close the menu
                return false
            }
        })
    }
}
