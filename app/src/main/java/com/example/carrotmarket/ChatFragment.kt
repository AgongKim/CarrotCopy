package com.example.carrotmarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.adapter.RecyclerAdapterChat
import com.example.carrotmarket.adapter.RecyclerAdapterHome

class ChatFragment(var CHK_LOGIN:Boolean):Fragment(){

    val itemsList : List<ItemsChat> = listOf(
        ItemsChat(null,"당근맨1","병점동","01-04","차빼이개새기야",R.drawable.test3),
        ItemsChat(null,"당근맨2","병점동","01-04","짜라란 짜라란 짜라란",R.drawable.test3),
        ItemsChat(null,"당근맨3","동동동","01-05","쿵짝짝쿵짝짝",R.drawable.test3)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        val adapter = RecyclerAdapterChat(itemsList)
        val recycler: RecyclerView = view.findViewById(R.id.verticalList)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        return view
    }
}
class ItemsChat(val profilePic:Int?,val id:String,val addr:String,val time:String,val lastChat:String,val itemPic:Int)