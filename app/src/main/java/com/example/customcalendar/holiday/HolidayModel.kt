package com.example.customcalendar.holiday

data class HolidayModel(
    val localdate : String = "", // 날짜
    val isHoliday : String = "", // 휴일인지
    val dateName : String = "" // 이름
)
