package com.sudo.data.util

import com.sudo.data.models.UserDB
import com.sudo.domain.entities.User
import java.util.Date
import java.util.Calendar

fun calculateDate(date: Date): Int{
    val now = Calendar.getInstance().get(Calendar.YEAR)
    val born = Calendar.getInstance()
    born.time = date
    return now - born.get(Calendar.YEAR)
}



fun UserDB.toUser(): User{
    return User(
        userId = this.userId,
        firstName = this.firstName,
        lastName = this.lastName,
        city = this.city,
        country = this.country,
        primarySport = this.primarySport,
        gender = this.gender,
        birthday = this.birthday,
        age = calculateDate(birthday).toByte(),
        height = this.height,
        wight = this.wight,
        imageUrl = this.imageUrl
    )
}