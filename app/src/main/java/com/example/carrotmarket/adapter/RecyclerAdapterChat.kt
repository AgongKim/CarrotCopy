package com.example.carrotmarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.ItemsChat
import com.example.carrotmarket.R
import kotlinx.android.synthetic.main.chat_items.view.*
import kotlinx.android.synthetic.main.home_items.view.imgProfile
import kotlinx.android.synthetic.main.home_items.view.txtAddr
import kotlinx.android.synthetic.main.home_items.view.txtTitle
import kotlinx.android.synthetic.main.home_items.view.txtTime

class RecyclerAdapterChat(private val items: List<ItemsChat>):
    RecyclerView.Adapter<RecyclerAdapterChat.ItemViewHolder>(){

    //뷰홀더 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //인플레이터로 뷰 생성
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.chat_items,parent,false)
        //생성된 뷰에 값을 넣어주기 위해 뷰홀더 콜
        return ItemViewHolder(inflatedView)
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

    class ItemViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private var view :View = v

        fun bind(item : ItemsChat){
            view.txtId.text = item.id
            view.txtAddr.text = item.addr
            view.txtTime.text = item.time
            view.txtLastChat.text = item.lastChat
            if(item.profilePic==null) view.imgProfile.setImageResource(R.drawable.profile) else view.imgProfile.setImageResource(item.profilePic)
            view.imgItem.setImageResource(item.itemPic)
        }
    }

}