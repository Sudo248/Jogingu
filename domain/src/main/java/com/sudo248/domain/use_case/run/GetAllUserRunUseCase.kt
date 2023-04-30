package com.sudo248.domain.use_case.run

import com.sudo248.domain.repositories.RunRepository

class GetAllUserRunUseCase(
    private val repo: RunRepository
) {
    suspend operator fun invoke() = repo.getAllUserRun()
}