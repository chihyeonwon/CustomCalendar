package com.example.customcalendar.individual

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
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