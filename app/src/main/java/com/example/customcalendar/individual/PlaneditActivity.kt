package com.example.customcalendar.individual

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.R
import com.example.customcalendar.databinding.ActivityEditplanBinding
import com.example.customcalendar.utils.FBAuth
import com.example.customcalendar.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PlaneditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditplanBinding
    private  val TAG = IndividualActivity::class.java.simpleName
    private lateinit var selectedbtn: String
    private lateinit var key:String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editplan)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_editplan)
        key = intent.getStringExtra("keyValue").toString()

        getPlan(key)

        //findViewById<TextView>(R.id.startDate).text = intent.getStringExtra("day").toString()
        //findViewById<TextView>(R.id.endDate).text = intent.getStringExtra("day").toString()

        binding.startDate.setOnClickListener { // 시작 날짜 설정
            val startView = LayoutInflater.from(this).inflate(R.layout.select_date, null)
            val sBuilder = AlertDialog.Builder(this).setView(startView).setTitle("시작일")
            val startAlertDialog = sBuilder.show()
            val dPicker = startView.findViewById<DatePicker>(R.id.dateSpinner)
            val acceptButton = startView.findViewById<Button>(R.id.acceptButton)
            val closeButton = startView.findViewById<Button>(R.id.closeButton)

            acceptButton.setOnClickListener {// 확인 버튼 처리
                val selectedYear = dPicker.year.toString()
                val selectedMonth = (dPicker.month + 1).toString() // 0부터 시작하므로 1을 더해야 실제 월을 얻을 수 있음.
                val selectedDay = dPicker.dayOfMonth.toString()
                //Log.d(TAG, "Selected Date: $selectedDate")
                findViewById<TextView>(R.id.startDate).text = selectedYear + "년 " + selectedMonth +"월 "+ selectedDay + "일"
                startAlertDialog.dismiss()
                //Toast.makeText(this, "수락", Toast.LENGTH_SHORT).show()
            }
            closeButton.setOnClickListener {// 취소버튼 처리
                startAlertDialog.dismiss()
            }
        }

        binding.endDate.setOnClickListener { // 종료 날짜 설정
            val endView = LayoutInflater.from(this).inflate(R.layout.select_date, null)
            val eBuilder = AlertDialog.Builder(this).setView(endView).setTitle("종료일")
            val dPicker = endView.findViewById<DatePicker>(R.id.dateSpinner)
            val endAlertDialog = eBuilder.show()

            val acceptButton = endView.findViewById<Button>(R.id.acceptButton)
            val closeButton = endView.findViewById<Button>(R.id.closeButton)

            acceptButton.setOnClickListener {// 확인 버튼 처리
                val selectedYear = dPicker.year.toString()
                val selectedMonth = (dPicker.month + 1).toString() // 0부터 시작하므로 1을 더해야 실제 월을 얻을 수 있음.
                val selectedDay = dPicker.dayOfMonth.toString()

                findViewById<TextView>(R.id.endDate).text = selectedYear + "년 " + selectedMonth +"월 "+ selectedDay + "일"
                endAlertDialog.dismiss()

            }
            closeButton.setOnClickListener {// 취소버튼 처리
                endAlertDialog.dismiss()
            }
        }

        binding.startTime.setOnClickListener { // 종료 날짜 설정
            val View = LayoutInflater.from(this).inflate(R.layout.select_time, null)
            val Builder = AlertDialog.Builder(this).setView(View).setTitle("종료일")
            val tPicker = View.findViewById<TimePicker>(R.id.timeSpinner)
            val endAlertDialog = Builder.show()

            val acceptButton = View.findViewById<Button>(R.id.acceptButton)
            val closeButton = View.findViewById<Button>(R.id.closeButton)

            acceptButton.setOnClickListener {// 확인 버튼 처리
                val selectedhour = tPicker.hour.toString()
                val selectedmin = tPicker.minute.toString()
                findViewById<TextView>(R.id.startTime).text = selectedhour + "시 " + selectedmin + "분"
                endAlertDialog.dismiss()
            }
            closeButton.setOnClickListener {// 취소버튼 처리
                endAlertDialog.dismiss()
            }
        }

        binding.endTime.setOnClickListener { // 종료 날짜 설정
            val View = LayoutInflater.from(this).inflate(R.layout.select_time, null)
            val Builder = AlertDialog.Builder(this).setView(View).setTitle("종료일")
            val tPicker = View.findViewById<TimePicker>(R.id.timeSpinner)
            val endAlertDialog = Builder.show()

            val acceptButton = View.findViewById<Button>(R.id.acceptButton)
            val closeButton = View.findViewById<Button>(R.id.closeButton)

            acceptButton.setOnClickListener {// 확인 버튼 처리
                val selectedhour = tPicker.hour.toString()
                val selectedmin = tPicker.minute.toString()
                findViewById<TextView>(R.id.endTime).text = selectedhour + "시 " + selectedmin + "분"
                endAlertDialog.dismiss()
            }
            closeButton.setOnClickListener {// 취소버튼 처리
                endAlertDialog.dismiss()
            }
        }

        binding.btnAccept.setOnClickListener {
            //val date = intent.getStringExtra("day").toString()
            val startdate = binding.startDate.text.toString()
            val enddate = binding.endDate.text.toString()
            val starttime = binding.startTime.text.toString()
            val endtime = binding.endTime.text.toString()
            val plan = binding.plan.text.toString()
            val location = binding.location.text.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val email = user?.email.toString()
            val inputTime = FBAuth.getTime()

            //FBRef.calendarRef.setValue(CalendarModel(date, plan, location))
            if(plan == "")
                Toast.makeText(binding.root.context, "일정이 없습니다.", Toast.LENGTH_SHORT).show()
            else if(email == "null")
                Toast.makeText(binding.root.context, "비로그인 상태.", Toast.LENGTH_SHORT).show()
            else
                FBRef.calendarRef.child(key).setValue(CalendarModel(startdate, enddate, starttime, endtime, plan, location, email, inputTime))
            //Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()

            Log.d(TAG, plan)

            finish()

        }

        binding.btnDelete.setOnClickListener {
            FBRef.calendarRef.child(key).removeValue()
            Toast.makeText(this,"삭제완료",Toast.LENGTH_LONG).show()
            finish()
        }

        binding.rdgroup.setOnCheckedChangeListener{ group, checkedID->
            selectedbtn = when(checkedID){
                R.id.high ->  "1"
                R.id.normal -> "2"
                R.id.low -> "3"
                else -> "NaN"
            }
        }
    }

    private fun getPlan(key: String) {
        val postListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val model = dataSnapshot.getValue(CalendarModel::class.java)
                binding.plan.setText(model?.plan)
                binding.startDate.setText(model?.startDate)
                binding.endDate.setText(model?.endDate)
                binding.startTime.setText(model?.startTime)
                binding.endTime.setText(model?.endTime)
                binding.location.setText(model?.location)

                when(model?.importance){
                    "1" -> binding.rdgroup.check(R.id.high)
                    "2" -> binding.rdgroup.check(R.id.normal)
                    "3" -> binding.rdgroup.check(R.id.low)
                }

                val user = FirebaseAuth.getInstance().currentUser
                val email = user?.email

                val writerUid = model?.email
                if (email.equals(writerUid)) {
                    binding.btnAccept.isClickable = true
                    binding.btnDelete.isClickable = true
                    binding.btnAccept.isVisible = true
                    binding.btnDelete.isVisible = true
                }
                else {
                    binding.btnAccept.isClickable = false
                    binding.btnDelete.isClickable = false
                    binding.btnAccept.isVisible = false
                    binding.btnDelete.isVisible = false
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.calendarRef.child(key).addValueEventListener(postListener)
    }

}