package com.sudo248.data.chat.api

import com.sudo248.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class RetrofitBuilder {

    fun buildClient() = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    })
    .addInterceptor { chain ->
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer "
                )
                .build()
        )
    }
    .build()

}