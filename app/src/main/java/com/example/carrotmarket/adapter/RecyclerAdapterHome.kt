package com.example.carrotmarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.ItemsHome
import com.example.carrotmarket.R
import kotlinx.android.synthetic.main.home_items.view.*

class RecyclerAdapterHome(private val items: List<ItemsHome>):
    RecyclerView.Adapter<RecyclerAdapterHome.ItemViewHolder>(){

    //뷰홀더 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //인플레이터로 뷰 생성
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.home_items,parent,false)
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
        var view :View = v

        fun bind(item : ItemsHome){
            view.txtTitle.text = item.title
            view.txtAddr.text = item.addr
            view.txtTime.text = item.time
            view.txtPrice.text = item.price.toString() + "원"
            view.imgProfile.setImageResource(item.itemPic)
        }
    }
}