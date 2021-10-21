package com.sudo.domain.entities

import java.util.Date

data class Activity(
    val activity_id: String,
    val name: String,
    val distance: Int, // m
    val pace: Short,
    val time_running: Int,
    val image: String,
    val time_start: Date,
    val location: String

)
