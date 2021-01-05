package com.example.carrotmarket

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    lateinit var context: Context

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}