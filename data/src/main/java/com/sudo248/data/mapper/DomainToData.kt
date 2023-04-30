package com.sudo248.data.mapper

import com.sudo248.data.local.database.models.NotificationDB
import com.sudo248.data.local.database.models.RunDB
import com.sudo248.data.local.database.models.firebase.RunDocument
import com.sudo248.data.local.database.models.firebase.UserDocument
import com.sudo248.domain.entities.Notification
import com.sudo248.domain.entities.Run
import com.sudo248.domain.entities.User

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
//        userImageUrl = this.userImageUrl
//    )
//}

internal fun Run.toRunDB(): RunDB {
    return RunDB(
        runId = this.runId,
        name = this.name,
        distance = this.distance,
        avgSpeed = this.avgSpeed,
        timeRunning = this.timeRunning,
        imageInByteArray = this.imageInByteArray,
        caloBurned = this.caloBurned,
        timeStart = this.timeStart,
        stepCount = this.stepCount,
        location = this.location,
        imageUrl = this.imageUrl
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

internal fun Run.toRunDocument(userId: String): RunDocument {
    return RunDocument(
        userId = userId,
        name = name,
        distance = distance,
        avgSpeed = avgSpeed,
        timeRunning = timeRunning,
        caloBurned = caloBurned,
        timeStart = timeStart,
        stepCount = stepCount,
        location = location,
        imageUrl = imageUrl
    )
}

internal fun User.toUserDocument(userId: String? = null): UserDocument {
    return UserDocument(
        userId = userId ?: this.userId,
        firstName = firstName,
        lastName = lastName,
        city = city,
        country = country,
        primarySport = primarySport,
        gender = gender,
        birthday = birthday,
        height = height,
        weight = weight,
        imageUrl = imageUrl
    )
}