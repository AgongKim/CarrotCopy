package com.example.carrotmarket

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.adapter.RecyclerAdapterHome

class HomeFragment() : Fragment() {
    //동네 == ㅁㅁ동 해서 해당되는 동의 아이템들만 검색 (HTTP통신)
    val itemsList : List<ItemsHome> = listOf(
        ItemsHome(R.drawable.test,"테스트","병점동","01-04",999999,4,2),
        ItemsHome(R.drawable.test2,"씹존예","병점동","01-04",999999,null,null),
        ItemsHome(R.drawable.test3,"틀니팝니다","동동동","01-05",20000,null,null)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var member = this.activity?.getSharedPreferences("member", Activity.MODE_PRIVATE)
        var CHK_LOGIN = member?.getBoolean("CHK_LOGIN",false)

        var view = inflater.inflate(R.layout.fragment_home,container,false)
        val adapter = RecyclerAdapterHome(itemsList)
        val recycler:RecyclerView = view.findViewById(R.id.verticalList)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        return view
    }
}

class ItemsHome(val itemPic:Int, val title:String, val addr:String, val time:String, val price:Int,
            val likes:Int?,val chats:Int?)