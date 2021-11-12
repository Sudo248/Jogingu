package com.sudo.data.repositories

import com.sudo.data.local.database.dao.JoginguDao
import com.sudo.data.mapper.*
import com.sudo.data.shared_preference.SharedPref
import com.sudo.data.util.calculateAge
import com.sudo.domain.common.Result
import com.sudo.domain.entities.*
import com.sudo.domain.entities.Target
import com.sudo.domain.repositories.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl(
    private val dao: JoginguDao,
    private val pref: SharedPref
) : MainRepository{

    override suspend fun setUser(user: User) {
        pref.setUser(user)
    }

    override suspend fun updateUser(user: User) {
        pref.setUser(user)
    }

    override suspend fun getUser(): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try{
            val user = pref.getUser()
            emit(Result.Success(user))
        }catch (e: IOException){
            emit(Result.Error("${e.message}"))
        }
    }

    override suspend fun getFullNameUser(): String {
        return pref.getLastNameUser() + pref.getFirstNameUser()
    }

    override suspend fun getBMRUser(): Float {
        val height = pref.getHeightUser()
        val weight = pref.getWeightUser()
        val gender = pref.getGenderUser()
        val age = pref.getBirthDayUser()
        return if(gender == Gender.FEMALE){
            (9.247f * weight) + (3.098f * height) - (4.33f * calculateAge(age)) + 447.593f
        }else{
            (13.397f * weight) + (4.799f * height) - (5.677f * calculateAge(age)) + 88.362f
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
    }.shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        replay = 1,
        started = SharingStarted.Lazily
    )

    override suspend fun addNewRun(run: Run) {
        dao.insertRunDB(run.toRunDB())
    }

    override suspend fun deleteRuns(vararg runs: Run) {
        runs.forEach { dao.deleteRunDBs(it.toRunDB()) }
    }

    override suspend fun getTarget(): Flow<Result<Target>> = flow {
        emit(Result.Loading)
        try{
            val target = pref.getTarget()
            emit(Result.Success(target))
        }catch (e: IOException){
            emit(Result.Error("${e.message}"))
        }
    }

    override suspend fun setTarget(target: Target) {
        pref.setTarget(target)
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