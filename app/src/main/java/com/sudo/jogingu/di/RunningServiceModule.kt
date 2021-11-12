package com.sudo.jogingu.di

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sudo.jogingu.R
import com.sudo.jogingu.common.Constant.ACTION_PAUSE
import com.sudo.jogingu.common.Constant.ACTION_RUNNING
import com.sudo.jogingu.common.Constant.NOTIFICATION_CHANNEL_ID
import com.sudo.jogingu.service.GoogleMapService
import com.sudo.jogingu.ui.activities.run.RunActivity
import com.sudo.jogingu.util.TimeUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Named

@Module
@InstallIn(ServiceComponent::class)
object RunningServiceModule {

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat = NotificationManagerCompat.from(context)

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        @Named("MainActivity")pendingIntent: PendingIntent
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_24)
            .setContentTitle(TimeUtil.parseTime(0) + " - "+"0 km")
            .setContentIntent(pendingIntent)


    @ServiceScoped
    @SuppressLint("UnspecifiedImmutableFlag")
    @Provides
    @Named("MainActivity")
    fun provideMainActivityPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, RunActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        },
        FLAG_UPDATE_CURRENT
    )

    @SuppressLint("UnspecifiedImmutableFlag")
    @ServiceScoped
    @Provides
    @Named("PauseService")
    fun providePauseServicePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getService(
        context,
        1,
        Intent(context, GoogleMapService::class.java).apply {
            action = ACTION_PAUSE
        },
        FLAG_UPDATE_CURRENT
    )

    @SuppressLint("UnspecifiedImmutableFlag")
    @ServiceScoped
    @Provides
    @Named("RunningService")
    fun provideRunningServicePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getService(
        context,
        2,
        Intent(context, GoogleMapService::class.java).apply {
            action = ACTION_RUNNING
        },
        FLAG_UPDATE_CURRENT
    )

    @SuppressLint("VisibleForTests")
    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @ServiceScoped
    fun provideSensorManager(
        @ApplicationContext context: Context
    ): SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
}