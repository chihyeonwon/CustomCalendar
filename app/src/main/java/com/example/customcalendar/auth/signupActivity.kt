package com.example.customcalendar.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.calendar.MainActivity
import com.example.customcalendar.R
import com.example.customcalendar.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class signupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding:ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup)

        binding.signupBtn.setOnClickListener {

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            var isGoToJoin = true

            // 이메일 값이 비어있는지 확인
            if(email.isEmpty()){
                Toast.makeText(this,"이메일을 입력해주세요", Toast.LENGTH_LONG).show()
                var isGoToJoin = false
            }
            // password1 값이 비어있는지 확인
            if(password1.isEmpty()){
                Toast.makeText(this,"Password1을 입력해주세요", Toast.LENGTH_LONG).show()
                var isGoToJoin = false
            }
            // password2 값이 비어있는지 확인
            if(password2.isEmpty()){
                Toast.makeText(this,"Password2을 입력해주세요", Toast.LENGTH_LONG).show()
                var isGoToJoin = false
            }
            // password1과 password2 값이 같은지 확인
            if(!password1.equals(password2)){
                Toast.makeText(this,"비밀번호를 똑같이 입력해주세요", Toast.LENGTH_LONG).show()
                var isGoToJoin = false
            }
            // password1의 길이가 6보다 작은지 확인
            if(password1.length < 6) {
                Toast.makeText(this,"비밀번호를 6자리 이상으로 입력해주세요", Toast.LENGTH_LONG).show()
                var isGoToJoin = false
            }

            if(isGoToJoin) {
                auth.createUserWithEmailAndPassword(email,password1)
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this,"성공", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,"실패",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }


    }
}