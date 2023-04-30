package com.sudo248.domain.repositories

import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Account
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(account: Account): DataState<String>

    suspend fun signup(account: Account): DataState<String>

    suspend fun getCurrentAccount(): DataState<Account>

    suspend fun logout(userId: String)
}