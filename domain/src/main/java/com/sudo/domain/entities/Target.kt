package com.sudo.domain.entities

import java.util.Date

data class Target(
    val distance: Int = 0,
    val calo: Int = 0,
    val recursive: Date? = null
)
