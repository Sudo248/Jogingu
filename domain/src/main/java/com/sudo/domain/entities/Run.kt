package com.sudo.domain.entities
import java.util.Date

data class Run(
    val runId: String,
    val name: String,
    val distance: Float, // meter
    val avgSpeed: Float,
    val timeRunning: Int,
    // co the khong luu duoc anh
    val imageUrl: String,
    val caloBurned: Int,
    val timeStart: Date,
    val location: String
)
