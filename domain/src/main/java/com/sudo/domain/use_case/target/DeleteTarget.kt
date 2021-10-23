package com.sudo.domain.use_case.target

import com.sudo.domain.repository.MainRepository

class DeleteTarget(private val repo: MainRepository) {
    suspend operator fun invoke() = repo.deleteTarget()
}