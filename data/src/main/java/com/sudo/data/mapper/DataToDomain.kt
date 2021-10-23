package com.sudo.data.mapper

import com.sudo.data.local.database.models.NotificationDB
import com.sudo.data.local.database.models.RunDB
import com.sudo.data.local.database.models.UserDB
import com.sudo.data.util.calculateDate
import com.sudo.domain.entities.Notification
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.Target
import com.sudo.domain.entities.User

internal fun UserDB.toUser(): User {
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

internal fun RunDB.toRun(): Run {
    return Run(
        runId = this.runId,
        name = this.name,
        distance = this.distance,
        pace = this.pace,
        timeRunning = this.timeRunning,
        imageUrl =  this.imageUrl,
        caloBurned = this.caloBurned,
        timeStart = this.timeStart,
        location = this.location
    )
}

internal fun NotificationDB.toNotification(): Notification {
    return Notification(
        notificationId = this.notificationId,
        timeNotify = this.timeNotify,
        timeToRun = this.timeToRun,
        note = this.note
    )
}