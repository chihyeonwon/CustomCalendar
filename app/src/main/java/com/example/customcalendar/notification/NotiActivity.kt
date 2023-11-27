package com.example.customcalendar.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.R
import com.example.customcalendar.calendar.AdapterDay
import com.example.customcalendar.databinding.ActivityNotiBinding
import com.example.customcalendar.friend.FriendModel
import com.example.customcalendar.individual.CalendarModel
import com.example.customcalendar.individual.PlanAdapter
import com.example.customcalendar.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class NotiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotiBinding

    private val TAG = NotiActivity::class.java.simpleName

    private val notiList = mutableListOf<CalendarModel>()

    private lateinit var notiRVAdapter: NotiListLVAdapter

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email.toString()

    private val friendList = mutableListOf<FriendModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noti)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_noti)

        // NotiListLVAdpater와 연결
        notiRVAdapter = NotiListLVAdapter(this, notiList)
        binding.NotiListView.adapter = notiRVAdapter

        getFBfriendData()

        // 파이어베이스에서 설정 날짜를 가져오는 부분
        getNotiDayData()

        for(item in friendList) {
            Log.i(item.friendEmail, "세팅1")
            Log.i(item.myEmail, "세팅2")
        }

        binding.NotiListView.setOnItemClickListener { parent, view, position, id ->
            Toast
                .makeText(this,"${notiList[position].startDate} 날짜에 알람", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // 파이어베이스에서 설정 날짜를 가져오는 부분
    private fun getNotiDayData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                notiList.clear()

                for(dataModel in dataSnapshot.children) { // 자신의 데이터 먼저 가져옴
                    val item = dataModel.getValue(CalendarModel::class.java)

                    if(email == item?.email) { // 시작일 일치, 중복 아닌 경우
                        notiList.add(item!!)
                    }

                    for(list in friendList) {
                        if(list.friendEmail == item?.email){
                            notiList.add(item!!)
                        }
                    }

                }

                // notiRVAdapter 동기화
                notiRVAdapter.notifyDataSetChanged()

                // startDate를 기준으로 DDay가 빠른 순으로 정렬
                notiList.sortBy { it.startDate }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.calendarRef.addValueEventListener(postListner)

    }

    private fun getFBfriendData() {
        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                friendList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(FriendModel::class.java)

                    // 친구이메일에 내 이메일이 있거나 이메일에 내 이메일이 있어야 리스트에 추가함
                    if(item!!.myEmail == email) {
                        //Log.i(item.friendEmail, "친구")
                        friendList.add(item!!)
                        /*keyvalue.add(dataModel.key.toString())*/
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.friendRef.addValueEventListener(postListner)
    }
}

