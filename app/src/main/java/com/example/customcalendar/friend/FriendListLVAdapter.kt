package com.example.customcalendar.friend

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendar.R
import com.example.customcalendar.utils.FBAuth
import com.example.customcalendar.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FriendListLVAdapter(val friendList : MutableList<FriendModel>, val keyvalue : MutableList<String>): BaseAdapter() {

    private val TAG = FriendListLVAdapter::class.java.simpleName
    val user = FirebaseAuth.getInstance().currentUser

    val email = user?.email
    override fun getCount(): Int {
        return friendList.size
    }

    override fun getItem(position: Int): Any {
        return friendList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // view를 가져와서 item과 연결해주는 부분
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if(view== null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.friend_list_item, parent,false)
        }

        val switch = view?.findViewById<SwitchCompat>(R.id.switch1)
        val friendID = view?.findViewById<TextView>(R.id.friendID)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.friendList)
        friendID!!.text = friendList[position].friendEmail

        switch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                /*val intent = Intent(view?.context, FriendCalendarActivity::class.java)
                intent.putExtra("friendEmail", friendList[position].friendEmail.toString())
                view?.context?.startActivity(intent)*/

                FBRef
                    .friendRef
                    .child(keyvalue[position])
                    .setValue(
                        FriendModel(
                            email.toString(),
                            friendList[position].friendEmail,
                            "true"
                        )
                    )

                Toast.makeText(view?.context, "${friendList[position].friendEmail}의 일정 데이터를 불러옵니다.", Toast.LENGTH_SHORT).show()
            } else {

                FBRef
                    .friendRef
                    .child(keyvalue[position])
                    .setValue(
                        FriendModel(
                            email.toString(),
                            friendList[position].friendEmail,
                            "false"
                        )
                    )

                Toast.makeText(view?.context, "Switch is OFF", Toast.LENGTH_SHORT).show()
            }
        }

        return view!!
    }

}