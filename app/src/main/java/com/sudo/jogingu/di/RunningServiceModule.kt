package com.sudo.jogingu.di

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sudo.jogingu.common.Constant.ACTION_PAUSE
import com.sudo.jogingu.common.Constant.ACTION_RUNNING
import com.sudo.jogingu.service.GoogleMapService
import com.sudo.jogingu.ui.activities.run.RunActivity
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