package com.sudo.domain.use_case.profile

import com.sudo.domain.repositories.MainRepository

class GetNameUserUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.getFullNameUser()
}