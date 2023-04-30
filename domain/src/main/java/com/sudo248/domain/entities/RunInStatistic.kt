package com.sudo248.domain.entities

import java.util.Date

data class RunInStatistic(
    val runId: String,
    val timeRunning: Int,
    val caloBurned: Int,
    val day: Date,
    val distance: Float,
    val stepCount: Int,
    val label: String = ""
)
