package com.sudo.domain.repositories

import com.sudo.domain.common.Result
import com.sudo.domain.entities.Notification
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.Target
import com.sudo.domain.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface StatisticRepository {

    suspend fun getAllRuns(): Flow<Result<List<Run>>>

    suspend fun getRunToday(today: Date): Run

}