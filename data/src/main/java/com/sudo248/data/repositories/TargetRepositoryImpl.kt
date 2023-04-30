package com.sudo248.data.repositories

import com.sudo248.data.shared_preference.SharedPref
import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Target
import com.sudo248.domain.repositories.TargetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TargetRepositoryImpl @Inject constructor(
    private val pref: SharedPref,
) : TargetRepository {
    override suspend fun getTarget(): Flow<DataState<Target>> = flow {
        emit(DataState.Loading)
        try{
            val target = pref.getTarget()
            emit(DataState.Success(target))
        }catch (e: IOException){
            emit(DataState.Error("${e.message}"))
        }
    }

    override suspend fun setTarget(target: Target) {
        pref.setTarget(target)
    }

    override suspend fun deleteTarget() {
        pref.deleteTarget()
    }
}