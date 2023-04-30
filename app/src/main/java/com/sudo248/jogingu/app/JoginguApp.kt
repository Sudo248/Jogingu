package com.sudo248.jogingu.app


import android.app.*
import android.os.Build
import androidx.annotation.RequiresApi
import com.sudo248.jogingu.common.Constant
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*

@HiltAndroidApp
class JoginguApp : Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupNotificationChannel()
    }

    private fun setupNotificationChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        createNotificationChannel(
            notificationManager,
            Constant.NOTIFICATION_RUNNING_CHANNEL_ID,
            Constant.NOTIFICATION_RUNNING_CHANNEL_NAME,
            "This channel for notification running"
        )

        createNotificationChannel(
            notificationManager,
            Constant.NOTIFICATION_RUN_CHANNEL_ID,
            Constant.NOTIFICATION_RUN_CHANNEL_NAME,
            "This channel for notification before run"
        )

        createNotificationChannel(
            notificationManager,
            Constant.NOTIFICATION_EVERYDAY_CHANNEL_ID,
            Constant.NOTIFICATION_EVERYDAY_CHANNEL_NAME,
            "This channel for notification everyday"
        )
    }

    private fun createNotificationChannel(
        manager: NotificationManager,
        channelId: String,
        channelName: String,
        description: String? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = description

            manager.createNotificationChannel(channel)
        }
    }

}