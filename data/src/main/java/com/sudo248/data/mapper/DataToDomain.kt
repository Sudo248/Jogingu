package com.sudo248.data.mapper

import com.sudo248.data.local.database.models.NotificationDB
import com.sudo248.data.local.database.models.RunDB
import com.sudo248.data.local.database.models.firebase.RunDocument
import com.sudo248.data.local.database.models.firebase.UserDocument
import com.sudo248.domain.entities.Notification
import com.sudo248.domain.entities.Run
import com.sudo248.domain.entities.User

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
//        userImageUrl = this.userImageUrl
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
        location = this.location,
        imageUrl = this.imageUrl
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

internal fun RunDocument.toRun(runId: String): Run {
    return Run(
        runId = runId,
        name = this.name,
        distance = this.distance,
        avgSpeed = this.avgSpeed,
        timeRunning = this.timeRunning,
        imageInByteArray = null,
        caloBurned = this.caloBurned,
        timeStart = this.timeStart,
        stepCount = this.stepCount,
        location = this.location,
        imageUrl = this.imageUrl
    )
}

fun UserDocument.toUser(): User {
    return User(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        city = city,
        country = country,
        primarySport = primarySport,
        gender = gender,
        birthday = birthday,
        height = height,
        weight = weight,
        imageUrl = imageUrl,
    )
}