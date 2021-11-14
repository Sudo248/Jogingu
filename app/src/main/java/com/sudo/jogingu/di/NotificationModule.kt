package com.sudo.jogingu.di

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sudo.jogingu.R
import com.sudo.jogingu.common.Constant.ACTION_NOTIFICATION_EVERYDAY
import com.sudo.jogingu.common.Constant.ACTION_NOTIFICATION_RUN
import com.sudo.jogingu.common.Constant.NOTIFICATION_EVERYDAY_CHANNEL_ID
import com.sudo.jogingu.common.Constant.NOTIFICATION_RUNNING_CHANNEL_ID
import com.sudo.jogingu.common.Constant.NOTIFICATION_RUN_CHANNEL_ID
import com.sudo.jogingu.common.Constant.OPEN_FRAGMENT
import com.sudo.jogingu.service.NotificationService
import com.sudo.jogingu.ui.activities.main.MainActivity
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
object NotificationModule {

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat = NotificationManagerCompat.from(context)

    @ServiceScoped
    @Provides
    @Named("RunningNotificationBuilder")
    fun provideRunningNotificationBuilder(
        @ApplicationContext context: Context,
        @Named("MainActivity")pendingIntent: PendingIntent
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, NOTIFICATION_RUNNING_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_24)
            .setContentTitle(TimeUtil.parseTime(0) + " - "+"0 km")
            .setContentIntent(pendingIntent)

    @Provides
    @ServiceScoped
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("UnspecifiedImmutableFlag")
    @Provides
    @ServiceScoped
    @Named("AlarmEverydayPendingIntent")
    fun provideAlarmEverydayPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent
            = PendingIntent.getService(
        context,
        3,
        Intent(context, NotificationService::class.java).apply {
            action = ACTION_NOTIFICATION_EVERYDAY
        },
        0
    )

    @Provides
    @ServiceScoped
    @Named("AlarmRunPendingIntent")
    fun provideAlarmRunPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent
            = PendingIntent.getService(
        context,
        4,
        Intent(context, NotificationService::class.java).apply {
            action = ACTION_NOTIFICATION_RUN
        },
        0
    )

    @SuppressLint("UnspecifiedImmutableFlag")
    @Provides
    @ServiceScoped
    @Named("MainActivityOpenTargetFragment")
    fun provideMainActivityOpenTargetFragmentPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent
    = PendingIntent.getActivity(
        context,
        5,
        Intent(context, MainActivity::class.java).apply {
            putExtra(OPEN_FRAGMENT, 1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        },
        FLAG_UPDATE_CURRENT
    )

    @Provides
    @ServiceScoped
    @Named("RunNotificationBuilder")
    fun provideRunNotificationBuilder(
        @ApplicationContext context: Context,
        @Named("MainActivityOpenTargetFragment") pendingIntent: PendingIntent
    ): NotificationCompat.Builder
    = NotificationCompat.Builder(context, NOTIFICATION_RUN_CHANNEL_ID)


    @Provides
    @ServiceScoped
    @Named("EverydayNotificationBuilder")
    fun provideEverydayNotificationBuilder(
        @ApplicationContext context: Context,
        @Named("MainActivityOpenTargetFragment") pendingIntent: PendingIntent
    ): NotificationCompat.Builder
    = NotificationCompat.Builder(context, NOTIFICATION_EVERYDAY_CHANNEL_ID)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setSmallIcon(R.drawable.ic_target_24px)
        .setContentIntent(pendingIntent)



}