package com.example.customcalendar.individual

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.example.customcalendar.R

class PlanAdapter(val planList: MutableList<CalendarModel>): BaseAdapter()  {
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
            view = LayoutInflater.from(parent?.context).inflate(R.layout.plan_list, parent,false)
        }

        val plan = view?.findViewById<TextView>(R.id.itemPlan)
        val hidden = view?.findViewById<TextView>(R.id.hiddenValue)
        val importance = view?.findViewById<ImageView>(R.id.importanceColor)
        val thumbnail = view?.findViewById<LinearLayout>(R.id.thumbnail)

        plan!!.text = planList[position].plan
        hidden!!.text = planList[position].inputTime
        when(planList[position].importance){
            "1" -> importance!!.setImageResource(R.drawable.circle_red)
            "2" -> importance!!.setImageResource(R.drawable.circle_yellow)
            "3" -> importance!!.setImageResource(R.drawable.circle_green)
            else -> importance!!.setImageResource(R.drawable.circle)
        }

        return view!!
    }
}