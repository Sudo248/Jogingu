package com.sudo.domain.use_case.target

import com.sudo.domain.repositories.MainRepository

class DeleteTarget(private val repo: MainRepository) {
    suspend operator fun invoke() = repo.deleteTarget()
}