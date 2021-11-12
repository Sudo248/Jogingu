package com.sudo.data.mapper

import com.sudo.data.local.database.models.NotificationDB
import com.sudo.data.local.database.models.RunDB
import com.sudo.domain.entities.Notification
import com.sudo.domain.entities.Run

//internal fun UserDB.toUser(): User {
//    return User(
//        firstName = this.firstName,
//        lastName = this.lastName,
//        city = this.city,
//        country = this.country,
//        primarySport = this.primarySport,
//        gender = this.gender,
//        birthday = this.birthday,
//        age = calculateDate(birthday).toByte(),
//        height = this.height,
//        weight = this.wight,
//        imageUrl = this.imageUrl
//    )
//}

internal fun RunDB.toRun(): Run {
    return Run(
        runId = this.runId,
        name = this.name,
        distance = this.distance,
        avgSpeed = this.avgSpeed,
        timeRunning = this.timeRunning,
        imageInByteArray =  this.imageInByteArray,
        caloBurned = this.caloBurned,
        timeStart = this.timeStart,
        stepCount = this.stepCount,
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