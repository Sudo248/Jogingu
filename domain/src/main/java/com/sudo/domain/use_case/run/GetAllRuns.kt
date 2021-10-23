package com.sudo.domain.use_case.run

import com.sudo.domain.repository.MainRepository

class GetAllRuns(private val repo: MainRepository ) {
    suspend operator fun invoke() = repo.getAllRuns()
}