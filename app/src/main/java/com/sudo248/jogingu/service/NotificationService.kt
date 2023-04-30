package com.sudo248.jogingu.service

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri

import android.os.IBinder

import androidx.core.app.NotificationCompat

import com.sudo248.jogingu.R

import java.util.*
import android.app.NotificationManager
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.sudo248.jogingu.common.Constant.ACTION_NOTIFICATION_EVERYDAY
import com.sudo248.jogingu.common.Constant.ACTION_NOTIFICATION_RUN
import com.sudo248.jogingu.common.Constant.ACTION_SETUP_EVERYDAY_NOTIFICATION
import com.sudo248.jogingu.common.Constant.ACTION_SETUP_RUN_NOTIFICATION
import com.sudo248.jogingu.common.Constant.NOTIFICATION_EVERYDAY_CHANNEL_ID
import com.sudo248.jogingu.common.Constant.NOTIFICATION_EVERYDAY_CHANNEL_NAME
import com.sudo248.jogingu.common.Constant.NOTIFICATION_EVERYDAY_ID
import com.sudo248.jogingu.common.Constant.NOTIFICATION_RUN_CHANNEL_ID
import com.sudo248.jogingu.common.Constant.NOTIFICATION_RUN_CHANNEL_NAME
import com.sudo248.jogingu.common.Constant.NOTIFICATION_RUN_ID
import com.sudo248.jogingu.common.Constant.TIME_NOTIFICATION
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class NotificationService : Service(){

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    @Named("AlarmEverydayPendingIntent")
    lateinit var alarmEverydayPendingIntent: PendingIntent

    @Inject
    @Named("AlarmRunPendingIntent")
    lateinit var alarmRunPendingIntent: PendingIntent

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    @Named("RunNotificationBuilder")
    lateinit var notificationRunBuilder: NotificationCompat.Builder

    @Inject
    @Named("EverydayNotificationBuilder")
    lateinit var notificationEveryDayBuilder: NotificationCompat.Builder

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun  onCreate() {
        super.onCreate()
    }

    override fun  onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when(intent.action){
            ACTION_SETUP_EVERYDAY_NOTIFICATION ->{
                setupAlarmEveryday()
            }
            ACTION_SETUP_RUN_NOTIFICATION -> {
                val time = intent.getLongExtra(TIME_NOTIFICATION,0L)
                setupAlarmRun(time)
            }
            ACTION_NOTIFICATION_EVERYDAY -> {
                senNotificationEveryDay()
            }
            ACTION_NOTIFICATION_RUN -> {
                senNotificationRun()
            }
            else -> Unit
        }
        return START_STICKY
    }

    private fun senNotificationRun() {
        // notification truoc luc chay


        val bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.avatar)
        val uri :Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val attributes : AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        notificationRunBuilder
            .setSound(uri)
            .setLargeIcon(bitmap)

        startForeground(NOTIFICATION_RUN_ID, notificationRunBuilder.build())
    }

    private fun senNotificationEveryDay(){
        // notification hang ngay vao luc 6h sang


        startForeground(NOTIFICATION_EVERYDAY_ID, notificationEveryDayBuilder.build())
    }

    private fun setupAlarmEveryday(){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmEverydayPendingIntent
        )
    }

    private fun setupAlarmRun(time: Long) {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            AlarmManager.INTERVAL_DAY,
            alarmRunPendingIntent
        )
    }





}