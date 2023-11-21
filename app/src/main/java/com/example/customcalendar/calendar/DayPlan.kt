package com.example.customcalendar.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.customcalendar.R
import com.example.customcalendar.individual.CalendarModel

class DayPlan(val planList: MutableList<CalendarModel>): BaseAdapter()  {
    override fun getCount(): Int {
        return planList.size
    }

    override fun getItem(position: Int): Any {
        return planList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view== null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_date, parent,false)
        }

        val plan = view?.findViewById<TextView>(R.id.pl_plan)
        val time = view?.findViewById<TextView>(R.id.pl_time)
        val loc = view?.findViewById<TextView>(R.id.pl_loc)
        //val hidden = view?.findViewById<TextView>(R.id.hiddenValue)

        plan!!.text = planList[position].plan
        time!!.text = planList[position].startTime + " ~ " + planList[position].endTime
        loc!!.text = planList[position].location
        //hidden!!.text = planList[position].inputTime

        return view!!
    }


}