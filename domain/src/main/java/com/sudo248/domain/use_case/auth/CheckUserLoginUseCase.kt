package com.sudo248.domain.use_case.auth

import com.sudo248.domain.common.DataState
import com.sudo248.domain.repositories.AuthRepository

class CheckUserLoginUseCase(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(): Boolean {
        return when(authRepository.getCurrentAccount()) {
            is DataState.Success -> {
               true
            }
            else -> false
        }
    }
}