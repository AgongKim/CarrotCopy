package com.example.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.adapter.RecyclerAdapterChat
import com.example.carrotmarket.adapter.RecyclerAdapterMsg
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    val itemsList : List<String> = listOf(
        "시발","메세지","테스트중"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //여기에 통신을 통해 지난 메세지 목록 불러오기.

        val adapter = RecyclerAdapterMsg(itemsList)
        msgList.layoutManager = LinearLayoutManager(this)
        msgList.adapter = adapter
    }
}