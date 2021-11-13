package com.sudo.domain.use_case.statistic

import com.sudo.domain.repositories.MainRepository

class GetRunsThisWeekUseCase (
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.getRunsThisWeek()
}