package com.example.customcalendar.menu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.customcalendar.R
import com.example.customcalendar.menu.UserModel
import com.example.customcalendar.utils.FBAuth


class UserListLVAdapter(val userList : MutableList<UserModel>): BaseAdapter() {
    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // view를 가져와서 item과 연결해주는 부분
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if(view== null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.user_list_item, parent,false)
        }

        val userID = view?.findViewById<TextView>(R.id.userID)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.userList)
        userID!!.text = userList[position].userID

        return view!!
    }
}