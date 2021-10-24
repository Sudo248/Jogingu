package com.sudo.jogingu.app

import android.app.Application
import android.util.Log
import com.sudo.jogingu.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class JoginguApp : Application() {
    override fun onCreate() {
        super.onCreate()
//        if(BuildConfig.DEBUG){
//            Timber.plant(Timber.DebugTree())
//        }
        Timber.plant(Timber.DebugTree())
        Timber.d("Init Timber")
    }
}