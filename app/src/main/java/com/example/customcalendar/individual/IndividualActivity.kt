package com.example.customcalendar.individual

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.R
import com.example.customcalendar.databinding.ActivityIndividualBinding
import com.example.shared_calender.utils.FBRef
class IndividualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualBinding

    private val TAG = IndividualActivity::class.java.simpleName

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_individual)

        // 선택한 날짜를 받아와서 화면에 뿌려줌
        findViewById<TextView>(R.id.startDate).text = intent.getStringExtra("day").toString()
        findViewById<TextView>(R.id.endDate).text = intent.getStringExtra("day").toString()

        binding.startBtn.setOnClickListener { // 시작 날짜 설정
            val startView = LayoutInflater.from(this).inflate(R.layout.select_date, null)
            val sBuilder = AlertDialog.Builder(this).setView(startView).setTitle("시작일")

            val startAlertDialog = sBuilder.show()

            val acceptButton = startView.findViewById<Button>(R.id.acceptButton)
            val closeButton = startView.findViewById<Button>(R.id.closeButton)

            acceptButton.setOnClickListener {// 확인 버튼 처리
                Toast.makeText(this, "수락", Toast.LENGTH_SHORT).show()
            }
            closeButton.setOnClickListener {// 취소버튼 처리
                startAlertDialog.dismiss()
            }
        }

        binding.endBtn.setOnClickListener { // 종료 날짜 설정
            val endView = LayoutInflater.from(this).inflate(R.layout.select_date, null)
            val eBuilder = AlertDialog.Builder(this).setView(endView).setTitle("종료일")

            val endAlertDialog = eBuilder.show()

            val acceptButton = endView.findViewById<Button>(R.id.acceptButton)
            val closeButton = endView.findViewById<Button>(R.id.closeButton)

            acceptButton.setOnClickListener {// 확인 버튼 처리
                Toast.makeText(this, "수락", Toast.LENGTH_SHORT).show()
            }
            closeButton.setOnClickListener {// 취소버튼 처리
                endAlertDialog.dismiss()
            }
        }

        binding.writeBtn.setOnClickListener {
            val date = intent.getStringExtra("day").toString()
            val plan = binding.plan.text.toString()
            val location = binding.location.text.toString()

            FBRef.calendarRef.setValue(CalendarModel(date, plan, location))

            //Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()

            Log.d(TAG, plan)


            finish()
        }
    }
}