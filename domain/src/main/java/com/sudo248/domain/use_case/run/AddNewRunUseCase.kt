package com.sudo248.domain.use_case.run

import com.sudo248.domain.entities.Run
import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.repositories.RunRepository

class AddNewRunUseCase(
    private val repo: RunRepository
) {
    suspend operator fun invoke(run: Run){
        repo.addNewRun(run)
    }
}