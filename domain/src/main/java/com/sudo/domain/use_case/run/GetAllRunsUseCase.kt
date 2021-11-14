package com.sudo.domain.use_case.run

import com.sudo.domain.repositories.MainRepository

class GetAllRunsUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.getAllRuns()
}