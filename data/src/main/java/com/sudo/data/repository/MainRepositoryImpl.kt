package com.sudo.data.repository

import com.sudo.data.local.database.dao.JoginguDao
import com.sudo.data.mapper.*
import com.sudo.data.shared_preference.SharedPref
import com.sudo.domain.common.Result
import com.sudo.domain.entities.Notification
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.Target
import com.sudo.domain.entities.User
import com.sudo.domain.repository.MainRepository
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dao: JoginguDao,
    private val pref: SharedPref
) : MainRepository{

    override suspend fun setUser(user: User) {
        dao.insertUserDB(userDB = user.toUserDB())
    }

    override suspend fun updateUser(user: User) {
        dao.updateUserDB(userDB = user.toUserDB())
    }

    override suspend fun getUser(): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try{
            val userDB = dao.getUserDB()
            emit(Result.Success(userDB.toUser()))
        }catch (e: IOException){
            emit(Result.Error("${e.message}"))
        }
    }

    override suspend fun getAllRuns(): Flow<Result<List<Run>>> = flow {
        emit(Result.Loading)
        try{
            val runDBFlow = dao.getAllRunDBs()
            emitAll(
                runDBFlow.map { list ->
                    Result.Success(list.map { it.toRun() })
                }
            )
        }catch (e: IOException){
            emit(Result.Error("${e.message}"))
        }
    }

    override suspend fun addNewRun(run: Run) {
        dao.insertRunDB(run.toRunDB())
    }

    override suspend fun deleteRuns(vararg runs: Run) {
        runs.forEach { dao.deleteRunDBs(it.toRunDB()) }
    }

    override suspend fun getTarget(): Flow<Result<Target>> = flow {
        emit(Result.Loading)
        try{
            val distance = pref.getDistanceTarget()
            val calo = pref.getCaloTarget()
            val recursive = pref.getRecursiveTarget()
            emit(Result.Success(Target(distance, calo, recursive)))
        }catch (e: IOException){
            emit(Result.Error("${e.message}"))
        }
    }

    override suspend fun setTarget(target: Target) {
        pref.setDistanceTarget(target.distance)
        pref.setCaloTarget(target.calo)
        pref.setRecursiveTarget(target.recursive)
    }

    override suspend fun deleteTarget() {
        pref.setDistanceTarget(0)
        pref.setCaloTarget(0)
        pref.setRecursiveTarget(null)
    }

    override suspend fun getAllNotifications(): Flow<Result<List<Notification>>> = flow {
        emit(Result.Loading)
        try{
            val notificationDBs = dao.getAllNotificationDBs()
            emitAll(
                notificationDBs.map { list ->
                    Result.Success(list.map { it.toNotification() })
                }
            )

        }catch (e: IOException){
            emit(Result.Error("${e.message}"))
        }
    }

    override suspend fun addNewNotification(notification: Notification) {
        dao.insertNotificationDB(notification.toNotificationDB())
    }

    override suspend fun deleteNotification(vararg notifications: Notification) {
        notifications.forEach { dao.deleteNotificationDBs(it.toNotificationDB()) }
    }
}