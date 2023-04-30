package com.sudo248.domain.use_case.run

import com.sudo248.domain.entities.Run
import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.repositories.RunRepository

class DeleteRunsUseCase(
    private val repo: RunRepository
) {
    suspend operator fun invoke(vararg runs: Run){
        repo.deleteRuns(*runs)
    }
}