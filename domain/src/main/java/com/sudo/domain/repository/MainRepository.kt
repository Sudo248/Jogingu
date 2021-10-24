package com.sudo.domain.repository

import com.sudo.domain.common.Result
import com.sudo.domain.entities.Notification
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.User
import com.sudo.domain.entities.Target
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun setUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun getUser(): Flow<Result<User>>

    suspend fun getAllRuns(): Flow<Result<List<Run>>>
    suspend fun addNewRun(run: Run)
    suspend fun deleteRuns(vararg runs: Run)

    suspend fun getTarget(): Flow<Result<Target>>
    suspend fun setTarget(target: Target)
    suspend fun deleteTarget()

    suspend fun getAllNotifications(): Flow<Result<List<Notification>>>
    suspend fun addNewNotification(notification: Notification)
    suspend fun deleteNotification(vararg notifications: Notification)

}