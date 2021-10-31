package com.sudo.jogingu.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.os.Vibrator
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.sudo.jogingu.R
import com.sudo.jogingu.ui.fragments.target.TargetFragment
import java.util.*
import android.app.NotificationManager
import android.util.Log
import com.sudo.jogingu.Notifi.NotificationS


class NotificationService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun  onCreate() {
        super.onCreate()


    }

    override fun  onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
       sentNotification(intent)
        return START_STICKY;
    }
    private fun sentNotification(intent: Intent) {
        val notificationIntent = Intent(this, TargetFragment::class.java)
        notificationIntent.putExtra("HOUR", intent.getIntExtra("HOUR", 0))
        notificationIntent.putExtra("MINUTE", intent.getIntExtra("MINUTE", 0))
        notificationIntent.putExtra("ID", intent.getIntExtra("ID", -1))
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
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
    override fun  onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }
}