package com.example.customcalendar.auth

import android.content.Intent
import android.graphics.Paint
import android.graphics.Paint.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.calendar.MainActivity
import com.example.customcalendar.R
import com.example.customcalendar.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        val textView = findViewById<TextView>(R.id.signup)
        textView.paintFlags = textView.paintFlags or UNDERLINE_TEXT_FLAG

        binding.signup.setOnClickListener {
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {

            var email = binding.emailArea.text.toString()
            var password = binding.passwordArea.text.toString()

            if(password.isEmpty() && email.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task->

                        if(task.isSuccessful) {

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)


                            Toast.makeText(this,"로그인 성공", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this,"로그인 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}