package com.sudo248.domain.use_case.statistic

import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.repositories.RunRepository

class GetRunsThisDayUseCase(
    private val repo: RunRepository
) {
    suspend operator fun invoke() = repo.getRunsThisDay()
}