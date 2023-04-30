package com.sudo248.jogingu.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant
import com.sudo248.jogingu.ui.activities.main.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification: Notification? = when (intent.action) {
            Constant.ACTION_NOTIFICATION_EVERYDAY -> {
                createNotification(
                    context,
                    Constant.NOTIFICATION_EVERYDAY_CHANNEL_ID,
                    intent.getStringExtra(Constant.KEY_TITLE_NOTIFICATION) ?: "Today Target",
                    intent.getStringExtra(Constant.KEY_CONTENT_NOTIFICATION) ?: "",
                    R.drawable.ic_baseline_notifications_active_24
                )
            }
            Constant.ACTION_NOTIFICATION_RUN -> {
                createNotification(
                    context,
                    Constant.NOTIFICATION_RUN_CHANNEL_ID,
                    intent.getStringExtra(Constant.KEY_TITLE_NOTIFICATION) ?: "Remind",
                    intent.getStringExtra(Constant.KEY_CONTENT_NOTIFICATION) ?: "Don't forget run",
                    R.drawable.ic_baseline_notifications_active_24,
                    getPendingIntentOpenMainActivity(context)
                )
            }
            else -> null
        }
        notification?.let {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(Constant.NOTIFICATION_EVERYDAY_ID, it)
        }
    }

    private fun createNotification(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        icon: Int,
        pendingIntent: PendingIntent? = null
    ): Notification {
        val builder = NotificationCompat.Builder(context, channelId).apply {
            pendingIntent?.let {
                setContentIntent(it)
            }
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(icon)
            setDefaults(NotificationCompat.DEFAULT_SOUND)
            color = context.getColor(R.color.main_color_normal)
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setStyle(NotificationCompat.BigTextStyle())
        }
        return builder.build()
    }

    private fun getPendingIntentOpenMainActivity(context: Context) = PendingIntent.getActivity(
        context,
        5,
        Intent(context, MainActivity::class.java).apply {
            putExtra(Constant.OPEN_FRAGMENT, 1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}