package com.sudo.data.util

import java.util.Date
import java.util.Calendar
import java.util.UUID

fun genId(prefix: String = ""): String{
    return prefix + UUID.randomUUID()
}

fun calculateDate(date: Date): Int{
    val now = Calendar.getInstance().get(Calendar.YEAR)
    val born = Calendar.getInstance()
    born.time = date
    return now - born.get(Calendar.YEAR)
}


