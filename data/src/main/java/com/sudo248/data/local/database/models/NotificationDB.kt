package com.sudo248.data.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sudo248.data.util.genId
import java.util.Date

@Entity(tableName = "notifications")
data class NotificationDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "notification_id")
    val notificationId: String = genId("Notification"),
    @ColumnInfo(name = "time_notify")
    val timeNotify: Date = Date(System.currentTimeMillis()),
    val note: String = "Nothing",
    @ColumnInfo(name = "time_to_run")
    val timeToRun: String = "Today"
)
