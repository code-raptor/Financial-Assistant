package com.coderaptor.financial.assistant.app.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private val PREFS_NAME = "kotlincodes"
    lateinit var sharedPref: SharedPreferences
    private val IS_FIRST_RUN_PREF = "is_first_run" to false
    private val SAVING = "saving" to false
    private val SHOPPING_MONITOR = "monitor" to false
    private val ESTIMATE = "estimate" to false
    private val CURRENT_DATE = "cdate" to ""

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var firstRun: Boolean
        get() = sharedPref.getBoolean(IS_FIRST_RUN_PREF.first, IS_FIRST_RUN_PREF.second)
        set(value) = sharedPref.edit {
            it.putBoolean(IS_FIRST_RUN_PREF.first, value)
        }

    var saving: Boolean
        get() = sharedPref.getBoolean(SAVING.first, SAVING.second)
        set(value) = sharedPref.edit {
            it.putBoolean(SAVING.first, value)
        }

    var shoppingMonitor: Boolean
        get() = sharedPref.getBoolean(SHOPPING_MONITOR.first, SHOPPING_MONITOR.second)
        set(value) = sharedPref.edit {
            it.putBoolean(SHOPPING_MONITOR.first, value)
        }
    var estimate: Boolean
        get() = sharedPref.getBoolean(ESTIMATE.first, ESTIMATE.second)
        set(value) = sharedPref.edit {
            it.putBoolean(ESTIMATE.first, value)
        }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    var currentDate: String
        get() = sharedPref.getString(CURRENT_DATE.first, CURRENT_DATE.second)
        set(value) = sharedPref.edit {
            if (value != CURRENT_DATE.second) {
                it.putString(CURRENT_DATE.first, value)
            }
        }
    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
    }

    fun getValueBoolean(KEY_NAME: String): Boolean {
        return sharedPref.getBoolean(KEY_NAME, false)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }
}