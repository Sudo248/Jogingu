package com.sudo.data.local.database.dao

import androidx.room.*
import com.sudo.data.local.database.models.NotificationDB
import com.sudo.data.local.database.models.RunDB
import com.sudo.data.local.database.models.UserDB
import kotlinx.coroutines.flow.Flow

@Dao
interface JoginguDao {

    @Insert
    suspend fun insertUserDB(userDB: UserDB)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserDB(userDB: UserDB)

    @Query("SELECT * FROM user")
    suspend fun getUserDB(): UserDB

    @Insert
    suspend fun insertRunDB(runDB: RunDB)

    @Delete
    suspend fun deleteRunDBs(runDB: RunDB)

    @Query("SELECT * FROM runs")
    fun getAllRunDBs(): Flow<List<RunDB>>

    @Insert
    suspend fun insertNotificationDB(notificationDB: NotificationDB)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotificationDB(notificationDB: NotificationDB)

    @Delete
    suspend fun deleteNotificationDBs(notificationDB: NotificationDB)

    @Query("SELECT * FROM notifications")
    fun getAllNotificationDBs(): Flow<List<NotificationDB>>

}