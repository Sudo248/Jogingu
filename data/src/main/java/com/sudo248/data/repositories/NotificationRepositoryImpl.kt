package com.sudo248.data.repositories

import com.sudo248.data.local.database.dao.JoginguDao
import com.sudo248.data.mapper.toNotification
import com.sudo248.data.mapper.toNotificationDB
import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Notification
import com.sudo248.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val dao: JoginguDao,
) : NotificationRepository {
    override suspend fun getAllNotifications(): Flow<DataState<List<Notification>>> = flow {
        emit(DataState.Loading)
        try{
            val notificationDBs = dao.getAllNotificationDBs()
            emitAll(
                notificationDBs.map { list ->
                    DataState.Success(list.map { it.toNotification() })
                }
            )

        }catch (e: IOException){
            emit(DataState.Error("${e.message}"))
        }
    }

    override suspend fun addNewNotification(notification: Notification) {
        dao.insertNotificationDB(notification.toNotificationDB())
    }

    override suspend fun deleteNotification(vararg notifications: Notification) {
        notifications.forEach { dao.deleteNotificationDBs(it.toNotificationDB()) }
    }
}