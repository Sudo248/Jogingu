package com.sudo.domain.use_case.profile

import com.sudo.domain.repositories.MainRepository

class GetUserUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.getUser()
}