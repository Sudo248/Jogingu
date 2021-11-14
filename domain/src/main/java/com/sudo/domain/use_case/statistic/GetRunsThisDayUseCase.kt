package com.sudo.domain.use_case.statistic

import com.sudo.domain.repositories.MainRepository

class GetRunsThisDayUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.getRunsThisDay()
}