package com.sudo248.data.local.database.models.firebase

import java.util.*

data class RunDocument(
    val userId: String = "",
    val name: String = "Today",
    val distance: Float =  0.0f,
    val avgSpeed: Float = 0.0f,
    val timeRunning: Int = 0,
    val caloBurned: Int = 0,
    val timeStart: Date = Date(System.currentTimeMillis()),
    val stepCount: Int = 0,
    val location: String = "Viet Nam",
    val imageUrl: String? = null
)
