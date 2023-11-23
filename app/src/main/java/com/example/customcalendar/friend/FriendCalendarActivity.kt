package com.example.customcalendar.friend

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.customcalendar.R
import com.example.customcalendar.calendar.AdapterMonth
import com.example.customcalendar.databinding.ActivityFriendCalendarBinding
import com.example.customcalendar.individual.CalendarModel
import com.example.customcalendar.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FriendCalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFriendCalendarBinding

    private val calendarDataList = mutableListOf<CalendarModel>()

    private val TAG = FriendCalendarActivity::class.java.simpleName

    val user = FirebaseAuth.getInstance().currentUser

    val email = user?.email

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        val height = resources.displayMetrics.heightPixels
        super.onCreate(savedInstanceState)
        binding = ActivityFriendCalendarBinding.inflate(layoutInflater) // ViewBinding 초기화
        setContentView(binding.root)

        val friendEmail = intent.getStringExtra("friendEmail")

        findViewById<TextView>(R.id.friendEmail).text = "${friendEmail.toString()}의 캘린더입니다."

        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth(height)

        binding.customCalendar.apply { // ViewBinding을 통해 calendar_custom 레이아웃 요소에 접근
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE / 2)
        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.customCalendar)

    }//onCreate


    private fun getFBBoardData(friendEmail: String) {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // calendarDataList.clear()

                val dataModel = dataSnapshot.getValue(CalendarModel::class.java)
                findViewById<TextView>(R.id.plan).setText(dataModel?.plan)


                // calendarDataList.reverse()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.calendarRef.addValueEventListener(postListner)

    }
}