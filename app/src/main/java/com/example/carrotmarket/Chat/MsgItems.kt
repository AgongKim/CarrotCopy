package com.example.carrotmarket.Chat

import com.google.firebase.database.Exclude

data class MsgItems(
    var msg: String? = "",
    var timestamp: String? = "",
    var userId: String? = "",
    var userNick:String? = ""
){
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "msg" to msg,
            "timestamp" to timestamp,
            "userId" to userId,
            "userNick" to userNick
        )
    }
}