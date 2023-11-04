package com.example.customcalendar.calendar

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.customcalendar.R
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendar.board.BoardActivity
import com.example.customcalendar.board.BoardModel
import com.example.customcalendar.databinding.DayAdapterBinding
import com.example.customcalendar.individual.CalendarModel
import com.example.customcalendar.individual.IndividualActivity
import com.example.customcalendar.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6
    var selectedDate = LocalDate.now()
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    private val TAG:String = MainActivity::class.java.simpleName
    inner class DayView(val binding: DayAdapterBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val binding = DayAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false) // ViewBinding 초기화
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {

        holder.binding.itemDayLayout.setOnClickListener {
            val intent = Intent(holder.binding.root.context, IndividualActivity::class.java)
            intent.putExtra("day","${formatter.format(dayList[position])}")
            holder.binding.root.context.startActivity(intent)


            //Toast.makeText(holder.binding.root.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
        }

        holder.binding.itemDayText.text = dayList[position].date.toString()

        holder.binding.itemDayText.setTextColor(when(position % 7) {
            0 -> Color.RED
            6 -> Color.BLUE
            else -> Color.BLACK
        })

        if(tempMonth != dayList[position].month) {
            holder.binding.itemDayText.alpha = 0.4f
        }

        if(Calendar.getInstance().get(Calendar.MONTH) == dayList[position].month && Calendar.getInstance().get(Calendar.DATE) == dayList[position].date) { // 오늘 날짜 강조
            holder.binding.itemDayLayout.setBackgroundResource(R.drawable.round_border)
        } // 기존 날짜 강조

        getPlanData(holder, position)
    }

    private fun getPlanData(holder: DayView, position: Int) {

        val planListner = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dateformat = SimpleDateFormat("yyyy년 MM월 dd일")
                val currentdate = dateformat.format(dayList[position])

                for(dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CalendarModel::class.java)

                    //Log.d(TAG, item?.startDate.toString())

                    if(currentdate == item?.startDate)
                        holder.binding.plan.setText(item?.plan)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.calendarRef.addValueEventListener(planListner)

    }
    override fun getItemCount(): Int {
        return ROW * 7
    }
}