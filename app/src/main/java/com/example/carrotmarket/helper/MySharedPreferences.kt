package com.example.carrotmarket.helper

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences (context: Context){
    val PREFS_FILENAME = "prefs"
    val PREF_KEY_MEMBER = "member"
    val PREF_KEY_CHK_LOGIN = "CHK_LOGIN"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME,0)

    var member: String?
        get() = prefs.getString(PREF_KEY_MEMBER,"")
        set(value) = prefs.edit().putString(PREF_KEY_MEMBER,value).apply()

    var login : Boolean
        get() = prefs.getBoolean(PREF_KEY_CHK_LOGIN,false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_CHK_LOGIN,value).apply()
}