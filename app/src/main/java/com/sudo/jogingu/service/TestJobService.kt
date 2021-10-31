package com.sudo.jogingu.service

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.job.JobParameters

import androidx.test.core.app.ApplicationProvider.getApplicationContext

import android.content.Intent

import android.app.job.JobService
import android.content.Context
import androidx.core.app.NotificationCompat
import com.sudo.jogingu.Notifi.NotificationS
import com.sudo.jogingu.R
import com.sudo.jogingu.ui.fragments.target.TargetFragment
import java.util.*
import kotlin.concurrent.thread


class TestJobService : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        doSenNotification(params);
        return true
    }

    private fun doSenNotification(jobParameters: Any) {
        val intent = Intent(this, TargetFragment::class.java)
        intent.putExtra("ID", (1+System.currentTimeMillis()).hashCode())
        intent.putExtra("HOURS", 11)
        intent.putExtra("MINUTES", 2)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 19)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent)
        val notification: Notification =
            NotificationCompat.Builder(this, NotificationS.CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle("Báo thức")
                .setContentText("Ring Ring")
                .setSilent(true)
                .setSmallIcon(R.drawable.ic_outline_calendar_24)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

    companion object {
        private const val TAG = "SyncService"
    }
}