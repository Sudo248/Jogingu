package com.sudo.domain.entities
import java.util.Date

data class Run(
    val runId: String,
    val name: String,
    val distance: Int, // meter
    val pace: Float,
    val timeRunning: Int,
    val imageUrl: String,
    val caloBurned: Int,
    val timeStart: Date,
    val location: String
)
