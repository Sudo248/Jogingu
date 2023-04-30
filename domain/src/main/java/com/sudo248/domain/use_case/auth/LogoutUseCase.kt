package com.sudo248.domain.use_case.auth

import com.sudo248.domain.repositories.AuthRepository

class LogoutUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(userId: String) = repo.logout(userId)
}