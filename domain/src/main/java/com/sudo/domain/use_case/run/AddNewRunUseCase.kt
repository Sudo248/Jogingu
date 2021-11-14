package com.sudo.domain.use_case.run

import com.sudo.domain.entities.Run
import com.sudo.domain.repositories.MainRepository

class AddNewRunUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke(run: Run){
        repo.addNewRun(run)
    }
}