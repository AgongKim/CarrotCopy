package com.example.carrotmarket.myInfo.adpater

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.carrotmarket.R

class AdrressListAdpater (val context: Context, val addressList: ArrayList<String>)
    : BaseAdapter() {
    override fun getCount(): Int {
        return addressList.size
    }

    override fun getItem(position: Int): Any {
        return addressList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_search_location_item, null)
        val address = view.findViewById<TextView>(R.id.addressTextView)

        address.text = addressList[position]

        return view
    }

}