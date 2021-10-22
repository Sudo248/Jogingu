package com.sudo.data.mapper

import com.sudo.data.local.database.models.NotificationDB
import com.sudo.data.local.database.models.RunDB
import com.sudo.data.local.database.models.TargetDB
import com.sudo.data.local.database.models.UserDB
import com.sudo.domain.entities.Notification
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.Target
import com.sudo.domain.entities.User

internal fun User.toUserDB(): UserDB {
    return UserDB(
        userId = this.userId,
        firstName = this.firstName,
        lastName = this.lastName,
        city = this.city,
        country = this.country,
        primarySport = this.primarySport,
        gender = this.gender,
        birthday = this.birthday,
        height = this.height,
        wight = this.wight,
        imageUrl = this.imageUrl
    )
}

internal fun Target.toTargetDB(): TargetDB {
    return TargetDB(
        targetId = this.targetId,
        distance = this.distance,
        calo = this.calo,
        isDone = this.isDone
    )
}

internal fun Run.toRunDB(): RunDB {
    return RunDB(
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

internal fun Notification.toNotificationDB(): NotificationDB {
    return NotificationDB(
        notificationId = this.notificationId,
        timeNotify = this.timeNotify,
        timeToRun = this.timeToRun,
        note = this.note
    )
}