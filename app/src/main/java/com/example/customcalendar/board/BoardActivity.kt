package com.example.customcalendar.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.customcalendar.R
import com.example.customcalendar.databinding.ActivityBoardBinding
import com.example.shared_calender.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding

    private val TAG = BoardActivity::class.java.simpleName

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardRVAdapter: BoardListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board)

        // BoardListLVAdpater와 연결
        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            /*val intent = Intent(context,BoardInsideActivity::class.java)
            intent.putExtra("title",boardDataList[position].title)
            intent.putExtra("content",boardDataList[position].content)
            intent.putExtra("time",boardDataList[position].time)
            startActivity(intent)*/

            val intent = Intent(this,BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }

        binding.writeBtn.setOnClickListener{
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        getFBBoardData()
    }

    private fun getFBBoardData() {

        val postListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                // dataModel에 있는 데이터를 하나씩 가져오는 부분
                for(dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }

                boardKeyList.reverse()
                // 최신 게시글이 앞으로 오도록 리스트를 뒤집는다.
                boardDataList.reverse()

                // boardRVAdapter 동기화
                boardRVAdapter.notifyDataSetChanged()

                Log.d(TAG, boardDataList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListner)

    }
}