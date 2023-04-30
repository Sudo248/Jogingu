package com.sudo248.data.chat.model

import java.util.Date

data class Message(
    val senderId: String = "",
    val senderImage: String = "",
    val content: String = "",
    val sendAt: Date? = null,
)