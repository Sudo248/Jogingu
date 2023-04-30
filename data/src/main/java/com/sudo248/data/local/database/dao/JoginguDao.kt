package com.sudo248.data.local.database.dao

import androidx.room.*
import com.sudo248.data.local.database.models.NotificationDB
import com.sudo248.data.local.database.models.RunDB
import kotlinx.coroutines.flow.Flow

@Dao
interface JoginguDao {

    @Insert
    suspend fun insertRunDB(runDB: RunDB)

    @Delete
    suspend fun deleteRunDBs(runDB: RunDB)

    @Query("SELECT * FROM runs")
    fun getAllRunDBs(): Flow<List<RunDB>>

    @Query("SELECT * FROM runs WHERE time_start >= :time ORDER BY time_start")
    fun getRunsFromDay(time: Long): Flow<List<RunDB>>

    @Insert
    suspend fun insertNotificationDB(notificationDB: NotificationDB)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotificationDB(notificationDB: NotificationDB)

    @Delete
    suspend fun deleteNotificationDBs(notificationDB: NotificationDB)

    @Query("SELECT * FROM notifications")
    fun getAllNotificationDBs(): Flow<List<NotificationDB>>

}