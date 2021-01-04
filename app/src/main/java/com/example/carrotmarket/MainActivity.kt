package com.example.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.carrotmarket.adapter.RecyclerAdapterHome
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val itemsList : List<Items> = listOf(
        Items(R.drawable.test,"테스트","병점동","01-04",999999,4,2),
        Items(R.drawable.test2,"씹존예","병점동","01-04",999999,null,null),
        Items(R.drawable.test3,"틀니팝니다","동동동","01-05",20000,null,null)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = RecyclerAdapterHome(itemsList)
        verticalList.adapter = adapter
    }
}
class Items(val itemPic:Int, val title:String, val addr:String, val time:String, val price:Int,
            val likes:Int?,val chats:Int?)