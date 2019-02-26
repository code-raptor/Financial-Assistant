package com.coderaptors.pocapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dbHandler = DatabaseHandler(this)

        button_save.setOnClickListener {
            if (validation()){
                val user = Users(editText_firstName.text.toString(), editText_lastName.text.toString())
                var success: Boolean

                success = dbHandler.addUser(user)

                if (success){
                    val toast = Toast.makeText(this,"Saved Successfully", Toast.LENGTH_LONG).show()
                }
            }

        }

        button_delete.setOnClickListener {
            dbHandler.deleteAll()
        }

        button_show.setOnClickListener {
            var user = dbHandler.getAllUsers()
            textView_show.text = user
        }

    }
    private fun validation(): Boolean{
        var validate: Boolean

        if (editText_firstName.text.toString() != "" &&
                editText_lastName.text.toString() != ""){
            validate = true
        }else{
            validate = false
            val toast = Toast.makeText(this,"Fill all details", Toast.LENGTH_LONG).show()
        }

        return validate
    }
}