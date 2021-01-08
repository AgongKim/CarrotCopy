package com.example.carrotmarket.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.Chat.MsgItems
import com.example.carrotmarket.helper.MyApplication
import com.example.carrotmarket.R
import kotlinx.android.synthetic.main.msg_items.view.*

class RecyclerAdapterMsg(private var items: List<MsgItems>,val myId:String ):
    RecyclerView.Adapter<RecyclerAdapterMsg.ItemViewHolder>(){

    //뷰홀더 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //인플레이터로 뷰 생성
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.msg_items,parent,false)
        //생성된 뷰에 값을 넣어주기 위해 뷰홀더 콜
        return ItemViewHolder(inflatedView,myId)
    }

    // 리스트 갯수 리턴해주는 함수
    override fun getItemCount(): Int {
        return items.size
    }
    //이미 생성된 뷰홀더에 데이터 바인드
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item)
        }
    }

    class ItemViewHolder(v: View,val myId:String): RecyclerView.ViewHolder(v) {
        var view :View = v

        fun bind(item : MsgItems){
            //내가 작성한 글
            if(item.userId.equals(myId)){
                var layout:LinearLayout = view as LinearLayout
                layout.gravity = Gravity.RIGHT
                layout.txtMsg.background = ContextCompat.getDrawable(MyApplication.applicationContext(),R.drawable.background_msg_me)
                layout.txtMsg.setTextColor(Color.WHITE)
                view.txtMsg.text = item.msg
            }else{//상대방이 작성한 글
                var layout:LinearLayout = view as LinearLayout
                layout.gravity = Gravity.LEFT
                layout.txtMsg.background = ContextCompat.getDrawable(MyApplication.applicationContext(),R.drawable.background_msg_otherperson)
                layout.txtMsg.setTextColor(Color.BLACK)
                view.txtMsg.text = item.msg
            }

        }
    }

}