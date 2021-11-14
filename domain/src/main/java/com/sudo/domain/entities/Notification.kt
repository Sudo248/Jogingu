package com.sudo.domain.entities

import java.util.Date


data class Notification(
    val notificationId: String,
    val timeNotify: Date,
    val note: String,
    // chủ yêu để hiển thị.
    val timeToRun: String


)
