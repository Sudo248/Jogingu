package com.sudo248.domain.repositories

import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Run
import com.sudo248.domain.entities.RunInStatistic
import com.sudo248.domain.entities.UserRun
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    suspend fun getAllUserRun(): Flow<DataState<List<UserRun>>>
    suspend fun getAllRuns(): Flow<DataState<List<Run>>>
    suspend fun addNewRun(run: Run)
    suspend fun deleteRuns(vararg runs: Run)

    suspend fun getRunsThisDay(): Flow<DataState<List<RunInStatistic?>>>
    suspend fun getRunsThisWeek(): Flow<DataState<List<RunInStatistic?>>>
    suspend fun getRunsThisMonth(): Flow<DataState<List<RunInStatistic?>>>
}