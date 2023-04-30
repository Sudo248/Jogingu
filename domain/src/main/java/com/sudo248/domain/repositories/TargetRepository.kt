package com.sudo248.domain.repositories

import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Target
import kotlinx.coroutines.flow.Flow

interface TargetRepository {
    suspend fun getTarget(): Flow<DataState<Target>>
    suspend fun setTarget(target: Target)
    suspend fun deleteTarget()
}