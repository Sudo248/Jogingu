package com.sudo.domain.entities

import java.util.Date


data class Notification(
    val notificationId: String,
    val timeNotify: Date,
    val note: String,
    val timeToRun: String
)
