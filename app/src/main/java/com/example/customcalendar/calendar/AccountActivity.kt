package com.example.customcalendar.calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.customcalendar.R
import com.example.customcalendar.auth.LoginActivity
import com.example.customcalendar.databinding.ActivityAccountBinding
import com.example.customcalendar.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email.toString()

    val creationTimestamp = user?.metadata?.creationTimestamp
    val date = Date(creationTimestamp!!)
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    val formattedDate = formatter.format(date)

    private lateinit var binding: ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)

        binding.email.text = "${email}님의 정보"

        binding.joinDay.text = "가입일 : ${formattedDate}"

        binding.logoutBtn.setOnClickListener {

            auth.signOut()

            // MainActivity로 화면 이동
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)


            Toast.makeText(this,"로그아웃", Toast.LENGTH_LONG).show()
        }
    }
}