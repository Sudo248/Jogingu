package com.sudo248.domain.repositories

import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.*
import com.sudo248.domain.entities.Target
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun isFirstOpenApp(): Boolean
    suspend fun isFirstOpenApp(isFirstOpenApp: Boolean)
}