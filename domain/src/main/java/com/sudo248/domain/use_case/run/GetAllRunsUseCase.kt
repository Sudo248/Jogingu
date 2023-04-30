package com.sudo248.domain.use_case.run

import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.repositories.RunRepository

class GetAllRunsUseCase(
    private val repo: RunRepository
) {
    suspend operator fun invoke() = repo.getAllRuns()
}