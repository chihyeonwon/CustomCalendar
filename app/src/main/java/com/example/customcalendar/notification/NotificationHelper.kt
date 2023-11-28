package com.example.customcalendar.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.*
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.customcalendar.R
import com.example.customcalendar.calendar.MainActivity

class NotificationHelper(base: Context?): ContextWrapper(base) {
    // 채널 변수 만들기
    private val channelID: String = "channelID"
    private val channelNm: String = "channelName"

    init {
        //안드로이드버전이 오레오보다 크면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(this, MainActivity::class.java)
            startForegroundService(intent)
            // 채널 생성
            createChannel()
        }
    }

    // 채널 생성 함수
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // 객체 생성
        val channel: NotificationChannel =
            NotificationChannel(channelID, channelNm, NotificationManager.IMPORTANCE_DEFAULT)
        /*val player: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.tiny_button_push_sound)*/
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 설정
        channel.setSound(soundUri, AudioAttributes.Builder().build())
        // 설정
        channel.enableLights(true) // 빛
        channel.enableVibration(true) // 진동
        channel.lightColor = Color.RED
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        // 생성
        getManager().createNotificationChannel(channel)


    }

    fun getManager(): NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    // Notification 설정
    fun getChannelNotification(title:String, message:String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(title) // 제목
            .setContentText(message) // 내용
            .setSmallIcon(R.drawable.calendar) // 아이콘
            .setVibrate(longArrayOf(50,100))
    }
}