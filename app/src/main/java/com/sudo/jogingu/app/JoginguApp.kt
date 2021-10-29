package com.sudo.jogingu.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class JoginguApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.d("Init Timber")

    }

//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val serviceChannel = NotificationChannel(
//                "CHANNEL_ID",
//                "Contact Tracing Service",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val manager = getSystemService(
//                NotificationManager::class.java
//            )
//            manager.createNotificationChannel(serviceChannel)
//        }
//    }
}