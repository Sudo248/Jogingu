package com.sudo.domain.use_case.run

import com.sudo.domain.entities.Run
import com.sudo.domain.repositories.MainRepository

class DeleteRunsUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke(vararg runs: Run){
        repo.deleteRuns(*runs)
    }
}