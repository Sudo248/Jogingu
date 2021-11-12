package com.sudo.domain.use_case.statistic

import com.sudo.domain.repositories.MainRepository

class GetRunsThisMonthUseCase (
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.getRunsThisMonth()
}