package com.example.customcalendar.calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.customcalendar.R
import com.example.customcalendar.board.BoardActivity
import com.example.customcalendar.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.customcalendar.auth.LoginActivity
import com.example.customcalendar.individual.IndividualActivity
import com.example.customcalendar.seperate.DaySeperateActivity
import com.example.customcalendar.seperate.MonthSeperateActivity
import com.example.customcalendar.seperate.WeekSeperateActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var drawerLayout: DrawerLayout

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // ViewBinding 초기화
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)

        // 게시판페이지(BoardActivity)로 이동
        findViewById<ImageView>(R.id.board).setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)
        }

        // 하루(당일) 단위로 일정을 보여주는 페이지로 이동
        findViewById<Button>(R.id.day).setOnClickListener {
            val intent = Intent(this, DaySeperateActivity::class.java)
            startActivity(intent)
        }

        // 일주일 단위로 일정을 보여주는 페이지로 이동
        findViewById<Button>(R.id.week).setOnClickListener {
            val intent = Intent(this, WeekSeperateActivity::class.java)
            startActivity(intent)
        }

        // 월 단위로 일정을 보여주는 페이지로 이동
        findViewById<Button>(R.id.month).setOnClickListener {
            val intent = Intent(this, MonthSeperateActivity::class.java)
            startActivity(intent)
        }

        // 액션바에 toolbar 셋팅
        setSupportActionBar(toolbar)

        // 액션바 생성
        val actionBar: ActionBar? = supportActionBar

        // 뒤로가기 버튼 생성
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // 뒤로가기 버튼 이미지 변경
        actionBar?.setHomeAsUpIndicator(R.drawable.main_menu)

        actionBar?.setDisplayShowTitleEnabled(false)

        //네비게이션뷰 아이템 선택 이벤트
        navigationView.setNavigationItemSelectedListener(
            object : NavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {

                    when (item.itemId) {
                        R.id.nav_camera -> {
                            item.isChecked = true
                            displayMessage("selected camera")
                            drawerLayout.closeDrawers()
                            return true
                        }

                        R.id.nav_photo -> {
                            item.isChecked = true
                            displayMessage("selected photo")
                            drawerLayout.closeDrawers()
                            return true
                        }

                        R.id.nav_slideShow -> {
                            item.isChecked = true
                            displayMessage("selected slideShow")
                            drawerLayout.closeDrawers()
                            return true
                        }

                        else -> {
                            return true
                        }
                    }//when
                }// onNavigationItemSelected
            }//NavigationView.OnNavigationItemSelectedListener
        )//setNavigationItemSelectedListener

        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth()

        binding.customCalendar.apply { // ViewBinding을 통해 calendar_custom 레이아웃 요소에 접근
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE / 2)
        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.customCalendar)
    }//onCreate

    // 메시지 알림
    private fun displayMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    // 메뉴 선택 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //drawerLayout 펼치기
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}