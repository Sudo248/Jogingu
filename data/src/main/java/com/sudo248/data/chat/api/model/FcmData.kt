package com.sudo248.data.chat.api.model

import com.google.gson.annotations.SerializedName
import com.sudo248.data.chat.model.FirebaseMessage

data class FcmData(
    @SerializedName("data")
    val data: FirebaseMessage,
    @SerializedName("to")
    val to: String
)