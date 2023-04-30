package com.sudo248.data.chat.model

data class Conversation(
    val conversationId: String = "",
    val firstUserId: String = "",
    val secondUserId: String = "",
    val messages: MutableList<Message> = mutableListOf()
)