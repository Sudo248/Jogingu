package com.sudo.jogingu.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri

import android.os.IBinder

import android.widget.Toast

import androidx.core.app.NotificationCompat

import com.sudo.jogingu.R

import com.sudo.jogingu.app.JoginguApp
import java.util.*
import android.app.NotificationManager





class NotificationService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun  onCreate() {
        super.onCreate()


    }

    override fun  onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        doSenNotification()
        return START_STICKY;
    }

    private fun doSenNotification() {
        val notifyIntent = Intent(this, com.sudo.jogingu.ui.activities.main.MainActivity::class.java).apply {
            putExtra("check", 1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
//        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.set(Calendar.HOUR_OF_DAY, 15)
//        calendar.set(Calendar.MINUTE, 6)
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY,
//            pendingIntent)
        val bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.avatar)
        val uri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification: Notification =
            NotificationCompat.Builder(this, JoginguApp.CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle("Target")
                .setContentText("6 km buổi sáng")

                .setLargeIcon(bitmap)
                .setSound(uri)
                .setSmallIcon(R.drawable.ic_target_24px)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)
    }

    override fun  onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }
}