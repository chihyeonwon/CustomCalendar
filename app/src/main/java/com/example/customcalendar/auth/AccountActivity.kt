package com.example.customcalendar.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.R
import com.example.customcalendar.calendar.MainActivity
import com.example.customcalendar.databinding.ActivityAccountBinding
import com.example.customcalendar.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    private val TAG = AccountActivity::class.java.simpleName

    private val requestList = ArrayList<RequestModel>()

    private lateinit var requestRVAdatper: RequestListLVAdapter

    private lateinit var auth: FirebaseAuth

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email.toString()

    val creationTimestamp = user?.metadata?.creationTimestamp
    val date = Date(creationTimestamp!!)
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    val formattedDate = formatter.format(date)
    private val keyvalue = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)

        binding.email.text = "${email}님의 정보"

        binding.joinDay.text = "가입일 : ${formattedDate}"

        // BoardListLVAdpater와 연결
        requestRVAdatper = RequestListLVAdapter(requestList, keyvalue)
        binding.requestListView.adapter = requestRVAdatper


        getFBRequestData()

        binding.logoutBtn.setOnClickListener {

            auth.signOut()

            // MainActivity로 화면 이동
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            user?.sendEmailVerification()


            Toast.makeText(this,"로그아웃", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFBRequestData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                requestList.clear()
                keyvalue.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(RequestModel::class.java)
                    if(item?.to.toString().equals(email.toString())) {
                        requestList.add(item!!)
                        keyvalue.add(dataModel.key.toString())
                    }
                }

                // userRVAdatper 동기화
                requestRVAdatper.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.requestRef.addValueEventListener(postListner)

    }
}