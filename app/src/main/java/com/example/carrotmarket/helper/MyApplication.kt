package com.example.carrotmarket.helper

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    lateinit var context: Context

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null
        lateinit var prefs : MySharedPreferences

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
    }
}