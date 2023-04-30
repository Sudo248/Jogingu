package com.sudo248.jogingu.util

import com.sudo248.data.chat.model.FirebaseMessage
import com.sudo248.data.chat.model.FirebaseMessageType
import com.sudo248.data.chat.model.Message

object MessageUtils {

    var receiverId = ""

    fun map(input: Map<String, String>): FirebaseMessage {
        return FirebaseMessage(
            type = FirebaseMessageType.valueOf(input["type"] ?: "MESSAGE"),
            senderId = input["senderId"] ?: receiverId,
            message = input["message"]?.let { convertFromString(it) } ?: Message()
        )
    }

}