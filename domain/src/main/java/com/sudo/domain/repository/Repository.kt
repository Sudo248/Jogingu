package com.sudo.domain.repository

import com.sudo.domain.common.Result
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.User
import com.sudo.domain.entities.Target
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun setUser(user: User)
    suspend fun getUser(): Flow<Result<User>>

    suspend fun getAllRun(): Flow<Result<List<Run>>>
    suspend fun addNewRun(run: Run)
    suspend fun deleteRun(run: Run)

    suspend fun getAllTarget(): Flow<Result<List<Target>>>
//    suspend fun getTarget


}