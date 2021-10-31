package com.sudo.jogingu.Notifi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class NotificationS : Application() {
    companion object{
        const val CHANNEL_ID = "Target"
        const val CHANNEL_ID2 = "ss"

    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // phai co khi ung dung chay android 8 tro len
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Target", NotificationManager.IMPORTANCE_DEFAULT)
            val channel2 = NotificationChannel(CHANNEL_ID2, "ss", NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null,null)
            channel2.setSound(null,null)
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(channel2)

        }
    }
}

