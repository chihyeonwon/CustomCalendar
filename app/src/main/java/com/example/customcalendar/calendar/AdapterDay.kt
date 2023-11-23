package com.example.customcalendar.calendar

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendar.R
import com.example.customcalendar.databinding.DayAdapterBinding
import com.example.customcalendar.holiday.HolidayModel
import com.example.customcalendar.individual.CalendarModel
import com.example.customcalendar.individual.IndividualActivity
import com.example.customcalendar.individual.PlanAdapter
import com.example.customcalendar.individual.PlaneditActivity
import com.example.customcalendar.utils.FBAuth
import com.example.customcalendar.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>, val height:Int, val holiday:MutableList<HolidayModel>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6
    var selectedDate = LocalDate.now()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val holiformat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private val keyvalue = mutableListOf<String>()
    private var tmpPosition = 0
    private val TAG = AdapterDay::class.java.simpleName
    inner class DayView(val binding: DayAdapterBinding): RecyclerView.ViewHolder(binding.root)

    val user = FirebaseAuth.getInstance().currentUser
    private val email = user?.email


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val binding = DayAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false) // ViewBinding 초기화
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {

        var isholi = 'F'
        holder.binding.itemDayLayout.setOnClickListener {
            val startView = LayoutInflater.from(holder.binding.root.context).inflate(R.layout.dayplan_dialog, null)
            val pBuilder = AlertDialog.Builder(holder.binding.root.context).setView(startView)
            val startAlertDialog = pBuilder.show()
            startAlertDialog.window?.setLayout(960,1280)
            val pdate = startView.findViewById<TextView>(R.id.date)
            val pdow = startView.findViewById<TextView>(R.id.DayOfWeek)
            val padd = startView.findViewById<ImageView>(R.id.btn_add)
            val hidden = startView.findViewById<TextView>(R.id.holiday_name)

            if(isholi.equals('T')) {
                pdate.setTextColor(Color.RED)
                hidden.text = holder.binding.holiday.text
            }
            /*11.21 ,수정사항*/
            val plist = startView.findViewById<ListView>(R.id.plan_list)


            plist.setOnItemClickListener{ parent, view, position, id ->
                val intent = Intent(holder.binding.root.context, PlaneditActivity::class.java)
                intent.putExtra("keyValue", keyvalue[position])
                tmpPosition = position
                holder.binding.root.context.startActivity(intent)
            }
            /*end*/
            pdate.text = dayList[position].date.toString()
            pdow.text = getDow(position)

            padd.setOnClickListener {
                val intent = Intent(holder.binding.root.context, IndividualActivity::class.java)
                intent.putExtra("day","${formatter.format(dayList[position])}")
                holder.binding.root.context.startActivity(intent)
            }


            val planData = mutableListOf<CalendarModel>()
            val Adapter = DayPlan(planData)
            val list = startView.findViewById<ListView>(R.id.plan_list)
            list.adapter = Adapter
            Adapter.notifyDataSetChanged()

            dayplan(holder, position, planData, Adapter)
            //Toast.makeText(holder.binding.root.context, "${height}", Toast.LENGTH_SHORT).show()
            //Toast.makeText(holder.binding.root.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
        }
        holder.binding.itemDayText.text = dayList[position].date.toString()
        holder.binding.itemDayLayout.layoutParams.height = height/8 //높이 조절

        val planData = mutableListOf<CalendarModel>()
        val planAdapter = PlanAdapter(planData)
        holder.binding.planList.adapter = planAdapter

        planAdapter.notifyDataSetChanged()

        holder.binding.itemDayText.setTextColor(when(position % 7) {
            0 -> Color.RED
            6 -> Color.BLUE
            else -> Color.BLACK
        })

        val tempdate = holiformat.format(dayList[position]).toString()
        //Log.i(tempdate, "날짜")
        for(item in holiday){
            if(tempdate.equals(item.localdate)){
                holder.binding.holiday.text = item.dateName
                holder.binding.holiday.textSize = holder.binding.hidden.textSize
                holder.binding.itemDayText.setTextColor(Color.RED)
                isholi = 'T'
            }
        }

        if(tempMonth != dayList[position].month) {
            holder.binding.itemDayText.alpha = 0.4f
        }

        if(Calendar.getInstance().get(Calendar.MONTH) == dayList[position].month && Calendar.getInstance().get(Calendar.DATE) == dayList[position].date) { // 오늘 날짜 강조
            holder.binding.itemDayLayout.setBackgroundResource(R.drawable.round_border)
        } // 기존 날짜 강조

        getPlanData(holder, position, planData, planAdapter) // 달력 셀에 데이터 가져오기
    }

    fun getDow(index: Int): String? {
        return when (index%7) {
            0 -> "일요일"
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            else -> "토요일"
        }
    }
    private fun getPlanData(holder: DayView, position: Int, planData: MutableList<CalendarModel>, planAdapter: PlanAdapter) { //파이어베이스에서 일정 가져오기

        val planListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일") // 비교할 문자열 형태
                val currentDate = formatter.format(dayList[position])

                planData.clear()

                for(dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CalendarModel::class.java)

                    if(currentDate == item?.startDate && email == item?.email )  // 시작일 일치, 중복 아닌경우
                        planData.add(item!!)
                }

                planAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.calendarRef.addValueEventListener(planListener)
    }
    private fun dayplan(holder: DayView, position: Int, planData: MutableList<CalendarModel>, planAdapter: DayPlan) { //파이어베이스에서 일정 가져오기

        val planListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일") // 비교할 문자열 형태
                val currentDate = formatter.format(dayList[position])

                planData.clear()
                keyvalue.clear()

                for(dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CalendarModel::class.java)

                    if(currentDate == item?.startDate && email == item?.email ) { // 시작일 일치, 중복 아닌경우
                        planData.add(item!!)
                        keyvalue.add(dataModel.key.toString())
                    }
                }

                planAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.calendarRef.addValueEventListener(planListener)
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}
