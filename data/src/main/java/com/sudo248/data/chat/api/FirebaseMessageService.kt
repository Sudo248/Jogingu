package com.sudo248.data.chat.api

import com.sudo248.data.chat.api.model.FcmData
import com.sudo248.data.chat.api.model.FcmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FirebaseMessageService {

    @POST("fcm/send")
    fun postSendFcm(@Body fcmData: FcmData): Response<FcmResponse>
}