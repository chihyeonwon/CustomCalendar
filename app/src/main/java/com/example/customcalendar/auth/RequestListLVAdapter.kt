package com.example.customcalendar.auth

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.customcalendar.R
import com.example.customcalendar.friend.FriendModel
import com.example.customcalendar.menu.UserModel
import com.example.customcalendar.utils.FBAuth
import com.example.customcalendar.utils.FBRef
import com.google.firebase.auth.FirebaseAuth

class RequestListLVAdapter(val requestList : MutableList<RequestModel>, val key1: String): BaseAdapter()
{
    private lateinit var auth: FirebaseAuth

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email.toString()
    override fun getCount(): Int {
        return requestList.size
    }

    override fun getItem(position: Int): Any {
        return requestList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // view를 가져와서 item과 연결해주는 부분
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = convertView
        if(view== null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.request_list_item, parent,false)
        }

        val requestID = view?.findViewById<TextView>(R.id.requestID)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.requestList)

        requestID!!.text = requestList[position].from


        // 친구 수락 버튼을 눌렀을 때
        view?.findViewById<Button>(R.id.accept)?.setOnClickListener {

            FBRef.friendRef.push()
                .setValue(FriendModel(email, requestList[position].from))

            FBRef.friendRef.push()
                .setValue(FriendModel(requestList[position].from, email))

            FBRef.requestRef.removeValue()

            /*if(requestID!!.text == requestList[position].from) {
                itemLinearLayoutView?.visibility=View.INVISIBLE*/
        }

        // 친구 거절 버튼을 눌렀을 때
        view?.findViewById<Button>(R.id.refuse)?.setOnClickListener {

            FBRef.requestRef.removeValue()
            /*if(requestID!!.text == requestList[position].from) {
                itemLinearLayoutView?.visibility=View.INVISIBLE
            }*/
        }

        return view!!
    }
}