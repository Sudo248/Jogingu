package com.sudo248.domain.repositories

import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getAllNotifications(): Flow<DataState<List<Notification>>>
    suspend fun addNewNotification(notification: Notification)
    suspend fun deleteNotification(vararg notifications: Notification)
}