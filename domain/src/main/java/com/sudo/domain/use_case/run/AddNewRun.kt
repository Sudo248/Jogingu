package com.sudo.domain.use_case.run

import com.sudo.domain.entities.Run
import com.sudo.domain.repository.MainRepository

class AddNewRun(private val repo: MainRepository) {
    suspend operator fun invoke(run: Run){
        repo.addNewRun(run)
    }
}