package com.sudo.data.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notifications")
data class NotificationDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "notification_id")
    val notificationId: String,
    @ColumnInfo(name = "time_notify")
    val timeNotify: Date,
    val note: String,
    @ColumnInfo(name = "time_to_run")
    val timeToRun: String
)
