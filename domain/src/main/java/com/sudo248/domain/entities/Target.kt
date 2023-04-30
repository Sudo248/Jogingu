package com.sudo248.domain.entities

import java.util.Date

data class Target(
    var distance: Int = 0,
    var calo: Int = 0,
    var place: String? = null,
    var recursive: Int = 0,
    var timeStart: Long = 0L,//"time_start_target"
    var notificationBefore: Int = 10
)
