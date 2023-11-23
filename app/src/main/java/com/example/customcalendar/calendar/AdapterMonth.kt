package com.example.customcalendar.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendar.BuildConfig.API_KEY
import com.example.customcalendar.databinding.MonthAdapterBinding
import com.example.customcalendar.holiday.HolidayModel
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class AdapterMonth(val height:Int): RecyclerView.Adapter<AdapterMonth.MonthView>() {
    val center = Int.MAX_VALUE / 2
    private var calendar = Calendar.getInstance()
    private val holiday = mutableListOf<HolidayModel>()

    inner class MonthView(val binding: MonthAdapterBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthView {
        val binding = MonthAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false) // ViewBinding 초기화
        return MonthView(binding)
    }

    override fun onBindViewHolder(holder: MonthView, position: Int) {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, position - center)

        holiday.clear()
        val thread = NetworkThread()
        thread.start()
        thread.join()

        holder.binding.monthText.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
        val tempMonth = calendar.get(Calendar.MONTH)

        var dayList: MutableList<Date> = MutableList(6 * 7) { Date() }

        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }
        val dayListManager = GridLayoutManager(holder.binding.root.context, 7)
        val dayListAdapter = AdapterDay(tempMonth, dayList, height, holiday)

        holder.binding.itemMonthDayList.apply {
            layoutManager = dayListManager
            adapter = dayListAdapter
        }

    }
    inner class NetworkThread: Thread(){
        override fun run() {
            var localdate : String = "" // 날짜
            var isHoliday : String = "" // 휴일인지
            var dateName : String = "" // 이름

            val solYear = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            var solMonth = month.toString()

            if(month<10)
                solMonth = "0" + month

            val site = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?serviceKey=" + API_KEY + "&solYear=" + solYear + "&solMonth=" + solMonth
            val xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(site)

            val list: NodeList = xml.getElementsByTagName("item")

            for(i in 0..list.length-1){

                val n:Node = list.item(i)

                if(n.getNodeType() == Node.ELEMENT_NODE){
                    val elem = n as Element
                    val map = mutableMapOf<String,String>()

                    // 이부분은 어디에 쓰이는지 잘 모르겠다.
                    for(j in 0..elem.attributes.length - 1) {
                        map.putIfAbsent(
                            elem.attributes.item(j).nodeName,
                            elem.attributes.item(j).nodeValue
                        )
                    }
                    localdate = elem.getElementsByTagName("locdate").item(0).textContent
                    isHoliday = elem.getElementsByTagName("isHoliday").item(0).textContent
                    dateName = elem.getElementsByTagName("dateName").item(0).textContent

                    var hdmodel = HolidayModel(localdate, isHoliday, dateName)

                    holiday.add(hdmodel)
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}