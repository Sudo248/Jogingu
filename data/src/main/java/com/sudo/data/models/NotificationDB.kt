package com.sudo.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notifications")
data class NotificationDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notification_id")
    val notificationId: Int,
    @ColumnInfo(name = "time_notify")
    val timeNotify: Date,
    val note: String,
    @ColumnInfo(name = "time_to_run")
    val timeToRun: String
)
