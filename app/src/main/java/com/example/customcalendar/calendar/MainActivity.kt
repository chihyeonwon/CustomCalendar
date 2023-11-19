package com.example.customcalendar.calendar

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.customcalendar.R
import com.example.customcalendar.board.BoardActivity
import com.example.customcalendar.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.example.customcalendar.auth.LoginActivity
import com.example.customcalendar.individual.CalendarModel
import com.example.customcalendar.individual.IndividualActivity
import com.example.customcalendar.menu.Menu1Activity
import com.example.customcalendar.utils.FBRef
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var drawerLayout: DrawerLayout

    private val calendarDataList = mutableListOf<CalendarModel>()

    private val TAG = MainActivity::class.java.simpleName

    val user = FirebaseAuth.getInstance().currentUser

    val email = user?.email

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        val height = resources.displayMetrics.heightPixels
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // ViewBinding 초기화
        setContentView(binding.root)

        Log.d(TAG,  binding.navigationView.getHeaderView(0)
            .findViewById<TextView>(R.id.account).text.toString())


        if(email == null) {
            binding.navigationView.getHeaderView(0)
                .findViewById<TextView>(R.id.account).text = "로그인해주세요"
        } else {
            binding.navigationView.getHeaderView(0)
                .findViewById<TextView>(R.id.account).text = email
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)

        // 계정 버튼 클릭했을 때 로그인 액티비티로 이동
        binding.navigationView.getHeaderView(0).setOnClickListener {
            val intent = if (FirebaseAuth.getInstance().currentUser != null) {
                Intent(this, AccountActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
        }


        // 게시판페이지(BoardActivity)로 이동
        findViewById<ImageView>(R.id.board).setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
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
                        R.id.account -> {
                            val intent = Intent(this@MainActivity, Menu1Activity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@MainActivity,"nav_camera",Toast.LENGTH_LONG).show()
                        }
                    }
                return false//when
                }// onNavigationItemSelected
            }//NavigationView.OnNavigationItemSelectedListener
        )//setNavigationItemSelectedListener

        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth(height)

        binding.customCalendar.apply { // ViewBinding을 통해 calendar_custom 레이아웃 요소에 접근
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE / 2)
        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.customCalendar)

        /*getFBBoardData()*/
    }//onCreate

    // 툴바 메뉴 버튼이 클릭 됐을 때 실행하는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭한 툴바 메뉴 아이템 id 마다 다르게 실행하도록 설정
        when(item!!.itemId){
            android.R.id.home->{
                // 햄버거 버튼 클릭시 네비게이션 드로어 열기
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 메시지 알림
    private fun displayMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    override fun onBackPressed() { //뒤로가기 처리
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers()
            // 테스트를 위해 뒤로가기 버튼시 Toast 메시지
            Toast.makeText(this,"back btn clicked",Toast.LENGTH_SHORT).show()
        } else{
            super.onBackPressed()
        }
    }


    private fun getFBBoardData() {

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
    class MyApplication : Application() {

        init{
            instance = this
        }

        companion object {
            lateinit var instance: MyApplication
            fun ApplicationContext() : Context {
                return instance.applicationContext
            }
        }
    }
}