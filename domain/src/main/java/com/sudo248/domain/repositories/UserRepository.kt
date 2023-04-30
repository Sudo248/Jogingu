package com.sudo248.domain.repositories

import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun setUser(user: User)
    suspend fun updateUser(user: User, isUpdateImage: Boolean)
    suspend fun getUser(): Flow<DataState<User>>
    suspend fun getFullNameUser(): String
    suspend fun getBMRUser(): Float
    suspend fun loadImageFromDevice(pathImage: String): Flow<DataState<ByteArray>>
}