package com.sudo248.data.chat.model

import com.sudo248.data.util.convertFromString

data class ConversationDocument(
    val conversationId: String = "",
    val firstUserId: String = "",
    val secondUserId: String = "",
    val messages: MutableList<String> = mutableListOf()
) {
    fun toConversation(): Conversation {
        return Conversation(
            conversationId = conversationId,
            firstUserId = firstUserId,
            secondUserId = secondUserId,
            messages = messages.map { convertFromString<Message>(it) }.toMutableList()
        )
    }
}