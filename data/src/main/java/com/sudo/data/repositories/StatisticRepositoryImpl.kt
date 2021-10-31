package com.sudo.data.repositories

import com.sudo.data.local.database.dao.JoginguDao
import com.sudo.data.mapper.toRun
import com.sudo.data.shared_preference.SharedPref
import com.sudo.domain.common.Result
import com.sudo.domain.entities.Run
import com.sudo.domain.repositories.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.*
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(
    private val dao: JoginguDao,
    private val pref: SharedPref
): StatisticRepository{

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
    }

    override suspend fun getRunToday(today: Date): Run {
        TODO("Not yet implemented")
    }



}