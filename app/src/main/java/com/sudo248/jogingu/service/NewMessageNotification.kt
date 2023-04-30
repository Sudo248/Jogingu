package com.sudo248.jogingu.service

data class NewMessageNotification(
    val notificationId: Int,
    val userString: String,
    val senderName: String?,
    val senderPhotoUrl: String?,
    val message: String?
)