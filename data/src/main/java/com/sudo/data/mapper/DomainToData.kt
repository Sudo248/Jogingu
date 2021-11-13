package com.sudo.data.mapper

import com.sudo.data.local.database.models.NotificationDB
import com.sudo.data.local.database.models.RunDB
import com.sudo.domain.entities.Notification
import com.sudo.domain.entities.Run

//internal fun User.toUserDB(): UserDB {
//    return UserDB(
//        userId = this.userId,
//        firstName = this.firstName,
//        lastName = this.lastName,
//        city = this.city,
//        country = this.country,
//        primarySport = this.primarySport,
//        gender = this.gender,
//        birthday = this.birthday,
//        height = this.height,
//        wight = this.weight,
//        imageUrl = this.imageUrl
//    )
//}

internal fun Run.toRunDB(): RunDB {
    return RunDB(
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

internal fun Notification.toNotificationDB(): NotificationDB {
    return NotificationDB(
        notificationId = this.notificationId,
        timeNotify = this.timeNotify,
        timeToRun = this.timeToRun,
        note = this.note
    )
}