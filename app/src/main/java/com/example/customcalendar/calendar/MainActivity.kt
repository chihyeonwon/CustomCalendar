package com.example.customcalendar.calendar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.customcalendar.R
import com.example.customcalendar.auth.AccountActivity
import com.example.customcalendar.auth.LoginActivity
import com.example.customcalendar.auth.RequestModel
import com.example.customcalendar.databinding.ActivityMainBinding
import com.example.customcalendar.friend.FriendListLVAdapter
import com.example.customcalendar.friend.FriendModel
import com.example.customcalendar.individual.CalendarModel
import com.example.customcalendar.menu.UserListLVAdapter
import com.example.customcalendar.menu.UserModel
import com.example.customcalendar.notification.NotiActivity
import com.example.customcalendar.utils.FBRef
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var drawerLayout: DrawerLayout

    private val TAG = MainActivity::class.java.simpleName

    val user = FirebaseAuth.getInstance().currentUser

    val email = user?.email

    private val userList = ArrayList<String>()

    private val requestList = mutableListOf<RequestModel>()

    private val friendList = mutableListOf<FriendModel>()

    private lateinit var friendRVAdatper: FriendListLVAdapter

    private lateinit var search :String

    private val keyvalue = mutableListOf<String>()


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        val height = resources.displayMetrics.heightPixels
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // ViewBinding 초기화
        setContentView(binding.root)

        if (email == null) {
            binding.navigationView.getHeaderView(0)
                .findViewById<TextView>(R.id.account).text = "로그인해주세요"
        } else {
            binding.navigationView.getHeaderView(0)
                .findViewById<TextView>(R.id.account).text = email
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)

        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                if(user != null) {
                    getFBfriendData()
                }
            }
        }
        getFBUserData()
        getFBRequestData()

        binding.addBtn.setOnClickListener {

            search = binding.search.text.toString()
            val myinf1:FriendModel = FriendModel(email.toString(), search, "true")
            val myinf2:FriendModel = FriendModel(email.toString(), search, "false")

            if(user != null) {
                if(friendList.contains(myinf1) || friendList.contains(myinf2))
                {
                    Toast
                        .makeText(this, "이미 친구입니다.", Toast.LENGTH_SHORT)
                        .show()
                } else if(email.toString() == search)
                {
                    Toast
                        .makeText(this,"자기 자신은 친구로 등록할 수 없습니다.",Toast.LENGTH_SHORT)
                        .show()
                }  else if(search == "") {
                    Toast
                        .makeText(this,"친구추가 할 이메일을 입력하세요.",Toast.LENGTH_SHORT)
                        .show()

                } else if(requestList.contains(RequestModel(email.toString(), search))) {
                    Toast
                        .makeText(this, "친구요청은 한 번만 가능합니다.",Toast.LENGTH_SHORT)
                        .show()
                } else if(!friendList.contains(FriendModel(email.toString(),search)) &&
                    !friendList.contains(FriendModel(search, email.toString())) &&
                    userList.contains(search)){
                    Toast
                        .makeText(this, "${search}에게 친구요청을 보냈습니다.",Toast.LENGTH_SHORT)
                        .show()
                    FBRef.requestRef
                        .push()
                        .setValue(RequestModel(email.toString(), search))
                } else {
                    Toast
                        .makeText(this, "없는 사용자입니다..", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "비로그인상태입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // FriendListLVAdapter와 연결
        friendRVAdatper = FriendListLVAdapter(friendList, keyvalue)

        binding.friendListView.adapter = friendRVAdatper

        // 계정 버튼 클릭했을 때 로그인 액티비티로 이동
        binding.navigationView.getHeaderView(0).setOnClickListener {
            val intent = if (FirebaseAuth.getInstance().currentUser != null) {
                Intent(this, AccountActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
        }

        // 알림 아이콘을 클릭했을 때 NotiActivity로 이동
        findViewById<ImageView>(R.id.noti).setOnClickListener {
            if(user != null) {
                val intent = Intent(this, NotiActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,"비로그인 상태입니다.",Toast.LENGTH_SHORT).show()
            }
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

        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth(height, friendList)

        binding.customCalendar.apply { // ViewBinding을 통해 calendar_custom 레이아웃 요소에 접근
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE / 2)
        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.customCalendar)
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

    override fun onBackPressed() { //뒤로가기 처리
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers()
        } else{
            super.onBackPressed()
        }
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

    private fun getFBRequestData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                requestList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(RequestModel::class.java)
                    requestList.add(item!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.requestRef.addValueEventListener(postListner)

    }


    private fun getFBUserData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                userList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(UserModel::class.java)
                    userList.add(item!!.userID)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userRef.addValueEventListener(postListner)

    }

    private suspend fun getFBfriendData() {
        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                friendList.clear()
                keyvalue.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(FriendModel::class.java)

                    // 친구이메일에 내 이메일이 있거나 이메일에 내 이메일이 있어야 리스트에 추가함
                    if(item!!.myEmail == email) {
                        //Log.i(item.friendEmail, "친구")
                        friendList.add(item!!)
                        keyvalue.add(dataModel.key.toString())
                    }
                }

                // userRVAdatper 동기화
                friendRVAdatper.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.friendRef.addValueEventListener(postListner)
    }

    /** Android 13 PostNotification */
    private fun checkAppPushNotification() {
        //Android 13 이상 && 푸시권한 없음
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)) {
            // 푸쉬 권한 없음
            permissionPostNotification.launch(Manifest.permission.POST_NOTIFICATIONS)
            return
        }

        //권한이 있을때
    }

    /** 권한 요청 */
    private val permissionPostNotification = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            //권한 허용
        } else {
            //권한 비허용
        }
    }


}