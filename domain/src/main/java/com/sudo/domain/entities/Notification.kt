package com.sudo.domain.entities

import java.util.Date


data class Notification(
    val notificationId: Int,
    val time: Date,
    val note: String
)
