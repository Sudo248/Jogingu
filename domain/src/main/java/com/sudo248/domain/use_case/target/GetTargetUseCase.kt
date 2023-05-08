package com.sudo248.domain.use_case.target

import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.repositories.TargetRepository

class GetTargetUseCase(
    private  val repo: TargetRepository
) {
    suspend operator fun invoke() = repo.getTarget()
}