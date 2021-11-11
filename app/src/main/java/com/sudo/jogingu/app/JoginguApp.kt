package com.sudo.jogingu.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi


import com.sudo.jogingu.R
import com.sudo.jogingu.service.NotificationReceiver
import com.sudo.jogingu.service.NotificationService
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class JoginguApp : Application() {
    companion object{
        const val CHANNEL_ID = "Target"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startJobService()
    }


    private fun startJobService() {
        val intent = Intent(this, NotificationService::class.java)
        val alarmPendingIntent: PendingIntent = PendingIntent.getService(this, 0, intent, 0)
        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 4)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmPendingIntent)
//        val intentService = Intent(this, NotificationService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            this.startForegroundService(intentService)
//        else
//            this.startService(intentService)
    }


    private fun createNotificationChannel() {
        // phai co khi ung dung chay android 8 tro len
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val uri :Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val attributes : AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            mChannel.setSound(uri,attributes)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}