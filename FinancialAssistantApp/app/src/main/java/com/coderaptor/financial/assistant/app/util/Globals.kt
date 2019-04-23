package com.coderaptor.financial.assistant.app.util

import android.annotation.SuppressLint
import android.app.Application

@SuppressLint("Registered")
class Globals : Application() {
    companion object {
        lateinit var instance: Globals
            private set
        var saving = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}