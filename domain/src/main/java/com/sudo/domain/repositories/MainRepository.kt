package com.sudo.domain.repositories

import com.sudo.domain.common.Result
import com.sudo.domain.entities.*
import com.sudo.domain.entities.Target
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun isFirstOpenApp(): Boolean
    suspend fun isFirstOpenApp(isFirstOpenApp: Boolean)

    suspend fun setUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun getUser(): Flow<Result<User>>
    suspend fun getFullNameUser(): String
    suspend fun getBMRUser(): Float

    suspend fun getAllRuns(): Flow<Result<List<Run>>>
    suspend fun addNewRun(run: Run)
    suspend fun deleteRuns(vararg runs: Run)

    suspend fun getRunsThisDay(): Flow<Result<List<RunInStatistic?>>>
    suspend fun getRunsThisWeek(): Flow<Result<List<RunInStatistic?>>>
    suspend fun getRunsThisMonth(): Flow<Result<List<RunInStatistic?>>>

    suspend fun getTarget(): Flow<Result<Target>>
    suspend fun setTarget(target: Target)
    suspend fun deleteTarget()

    suspend fun getAllNotifications(): Flow<Result<List<Notification>>>
    suspend fun addNewNotification(notification: Notification)
    suspend fun deleteNotification(vararg notifications: Notification)

}