package com.example.customcalendar.notification

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.example.customcalendar.R
import com.example.customcalendar.individual.CalendarModel
import java.text.SimpleDateFormat
import java.time.LocalDate


class NotiListLVAdapter(val context: Context, val notiList : MutableList<CalendarModel>): BaseAdapter() {

    private val TAG = NotiListLVAdapter::class.java.simpleName

    // Noti 객체 생성
    private lateinit var notificationHelper: NotificationHelper
    override fun getCount(): Int {
        return notiList.size
    }

    override fun getItem(position: Int): Any {
        return notiList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // view를 가져와서 item과 연결해주는 부분
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if(view== null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.noti_list_item, parent,false)
        }

        val DDay = view?.findViewById<TextView>(R.id.DDay)
        val notiPlan =view?.findViewById<TextView>(R.id.notiPlan)
        val Location = view?.findViewById<TextView>(R.id.location)
        val writer = view?.findViewById<TextView>(R.id.writer)

        // 오늘 날짜
        val currentDate = LocalDate.now()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse(currentDate.toString()).time
        val endDate = dateFormat.parse(notiList[position].startDate.toString()).time
        val diffInMilliSec = endDate - startDate
        var diffInDays = diffInMilliSec / (1000 * 60 * 60 * 24)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.notiList)
        notiPlan!!.text = "일정 : ${notiList[position].plan}"
        Location!!.text = "장소 : ${notiList[position].location}"
        writer!!.text = "작성자 : ${notiList[position].email}"

        // 만약에 DDay가 음수일 때는 절댓값을 취한뒤 + 기호를, 양수일 때는 -기호를 붙여서 출력한다.
        if (diffInDays < 0) {
            diffInDays = Math.abs(diffInDays)
            DDay!!.text = "D + ${diffInDays.toString()}"
        } else {
            DDay!!.text = "D - ${diffInDays.toString()}"
        }

        // Noti 초기화
        notificationHelper = NotificationHelper(context)


        if(notiList[position].startDate.contains(currentDate.toString())) {
            // 오늘 날짜와 notiList의 날짜가 같으면 알림을 발생한다.
            notiPlay(notiList[position].startDate.toString(),
                notiList[position].plan.toString(),
                notiList[position].startTime.toString(),
                notiList[position].email.toString())
        }

        return view!!
    }

    private fun notiPlay(Date: String, Plan: String, Time: String, Email: String){
        /*val title: String = titleEdit.text.toString()
        val message: String = messageEdit.text.toString()*/

        val title = "${Email}는 ${Date}에 일정이 있습니다."
        val message = "${Time}부터 ${Plan} 일정이 있습니다."

        // 알림 호출
        showNotification(title, message)
    }

    // 알림 호출
    private fun showNotification(title:String, message:String) {
        val nb: NotificationCompat.Builder =
            notificationHelper.getChannelNotification(title,message)
        notificationHelper.getManager().notify(1, nb.build())
    }
}