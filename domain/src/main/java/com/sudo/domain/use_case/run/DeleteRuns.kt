package com.sudo.domain.use_case.run

import com.sudo.domain.entities.Run
import com.sudo.domain.repository.MainRepository

class DeleteRuns(private val repo: MainRepository) {
    suspend operator fun invoke(vararg runs: Run){
        repo.deleteRuns(*runs)
    }
}