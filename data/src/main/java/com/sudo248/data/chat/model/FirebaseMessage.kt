package com.sudo248.data.chat.model

data class FirebaseMessage(
    val type: FirebaseMessageType = FirebaseMessageType.MESSAGE,
    val senderId: String = "",
    val message: Message = Message()
)
