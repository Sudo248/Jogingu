package com.sudo.domain.use_case.target

import com.sudo.domain.repository.MainRepository

class GetTarget(private  val repo: MainRepository) {
    suspend operator fun invoke() = repo.getTarget()
}