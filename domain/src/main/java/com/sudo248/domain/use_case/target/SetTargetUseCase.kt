package com.sudo248.domain.use_case.target

import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.entities.Target
import com.sudo248.domain.repositories.TargetRepository

class SetTargetUseCase(
    private val repo: TargetRepository
) {
    suspend operator fun invoke(target: Target){
        repo.setTarget(target)
    }
}