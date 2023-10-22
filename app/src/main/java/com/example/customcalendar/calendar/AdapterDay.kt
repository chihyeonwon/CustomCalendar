package com.example.customcalendar.calendar

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.customcalendar.R
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendar.databinding.DayAdapterBinding
import com.example.customcalendar.individual.IndividualActivity
import java.time.LocalDate
import java.util.*

class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6
    var selectedDate = LocalDate.now()
    inner class DayView(val binding: DayAdapterBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val binding = DayAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false) // ViewBinding 초기화
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {

        holder.binding.itemDayLayout.setOnClickListener {
            val intent = Intent(holder.binding.root.context, IndividualActivity::class.java)
            intent.putExtra("day","${dayList[position]}")
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
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}