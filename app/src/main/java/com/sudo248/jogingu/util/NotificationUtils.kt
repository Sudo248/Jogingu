package com.sudo248.jogingu.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import com.sudo248.jogingu.R
import com.sudo248.jogingu.service.NewMessageNotification
import com.sudo248.jogingu.ui.activities.main.MainActivity

object NotificationUtils {
    private val VERBOSE_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"
    private const val VERBOSE_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
    private const val CHANNEL_ID = "VERBOSE_NOTIFICATION"

    fun makeStatusNotification(
        context: Context,
        newNotification: NewMessageNotification
    ) {
        createNotificationChannel(context)
        val notification = getNotification(context, newNotification)
        NotificationManagerCompat.from(context).notify(newNotification.notificationId, notification)
    }

    private fun getNotification(
        context: Context,
        notification: NewMessageNotification
    ): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(getPendingIntent(context, notification.userString))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.senderName)
            .setContentText(notification.message)
            .setLargeIcon(getBitmapDrawable(context))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .build()
    }

    private fun getPendingIntent(context: Context, userString: String): PendingIntent? {
        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.putExtra("userString", userString)
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun getBitmapDrawable(context: Context): Bitmap? {
        return AppCompatResources.getDrawable(context, R.drawable.img_user)
            ?.toBitmap()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = VERBOSE_CHANNEL_NAME
            val description = VERBOSE_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            getNotificationManager(context)?.createNotificationChannel(channel)
        }
    }

    private fun getNotificationManager(context: Context): NotificationManager? {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
    }
}