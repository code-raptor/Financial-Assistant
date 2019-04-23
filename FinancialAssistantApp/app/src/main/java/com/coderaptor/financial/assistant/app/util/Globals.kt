package com.coderaptor.financial.assistant.app.util

import android.annotation.SuppressLint
import android.app.Application

@SuppressLint("Registered")
class Globals : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreference.init(this)
    }
}