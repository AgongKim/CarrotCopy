package com.example.carrotmarket

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.carrotmarket.Chat.MsgItems
import com.example.carrotmarket.adapter.RecyclerAdapterMsg
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    val itemsList : ArrayList<MsgItems> = ArrayList()

    //임시용 아이디
    private val myId = "th3121"
    private val myNick = "아공"
    //임시용 채팅방 키
    private val chatroom = "-MQG3T4XgvTj5Gt7F42B"
    private val database = Firebase.database
    private val myRef = database.getReference("Messages")
    lateinit var adapter: RecyclerAdapterMsg
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //채팅방 생성 (시간으로 생성된 키 자동할당됨)
//        val chatRoomRef = myRef.push()
//        chatRoomRef.setValue(itemsList)
//        Toast.makeText(this,chatRoomRef.key,Toast.LENGTH_SHORT).show()


        //여기에 통신을 통해 지난 메세지 목록 불러오기.


        //채팅목록을 띄울 리사이클러 뷰
        adapter = RecyclerAdapterMsg(itemsList,myId)
        msgList.layoutManager = LinearLayoutManager(this)
        msgList.adapter = adapter

        initDatabase()

        //버튼 리스너 달아서 채팅 디비에 저장
        btnInput.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                var timestamp = getTime()
                val mychatroom = myRef.child(chatroom).push()
                if(editMsg.text!=null){
                    var temp = MsgItems(editMsg.text.toString(),timestamp,myId,myNick)
                    mychatroom.setValue(temp)
                }
                editMsg.text = null
            }
        })
    }

    fun initDatabase(){
        myRef.child(chatroom).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                itemsList.clear()
                for(snapshot in snapshot.children) {
                    var item : MsgItems? = snapshot.getValue<MsgItems>()
                    if(item!=null) itemsList.add(item)
                }
                adapter.notifyDataSetChanged()
                msgList.scrollToPosition(itemsList.size-1)
            }
        })

        myRef.child(chatroom).addChildEventListener(object : ChildEventListener {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            override fun onChildAdded(dataSnapshot: DataSnapshot,s: String?) {

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                val item: MsgItems? = dataSnapshot.getValue<MsgItems>()

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                if(item!=null) itemsList.add(item)

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged()
                msgList.scrollToPosition(itemsList.size-1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getTime():String{
        val currentDateTime = Calendar.getInstance().time
        var dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA).format(currentDateTime)
        return dateFormat
    }
}