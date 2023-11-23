package com.example.customcalendar.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.R
import com.example.customcalendar.databinding.ActivityNotiBinding
import com.example.customcalendar.friend.FriendModel
import com.example.customcalendar.individual.CalendarModel
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

        // 파이어베이스에서 설정 날짜를 가져오는 부분
        getNotiDayData()

        // 친구 데이터를 받아옴
        getfriendNotiDayData()


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

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {


                    val item = dataModel.getValue(CalendarModel::class.java)
                    // 데이터베이스의 email과 현재 email이 같을 때 데이터를 리스트에 넣는다.
                    if(item!!.email == email) {
                        notiList.add(item!!)
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

    private fun getfriendNotiDayData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                notiList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {


                    val item = dataModel.getValue(CalendarModel::class.java)
                    // 데이터베이스의 email과 현재 email이 같을 때 데이터를 리스트에 넣는다.
                    if(item!!.email == email) {
                        notiList.add(item!!)
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
}

