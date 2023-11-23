//package com.example.shared_calender.utils
package com.example.customcalendar.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {

        private val database = Firebase.database

        val category1 = database.getReference("contents")

        val category2 = database.getReference("contents2")

        val bookmarkRef = database.getReference("bookmark_list")

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")

        val calendarRef = database.getReference("calendar")

        val userRef = database.getReference("user")

        val groupRef = database.getReference("group")

    }
}