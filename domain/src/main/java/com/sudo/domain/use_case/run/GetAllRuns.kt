package com.sudo.domain.use_case.run

import com.sudo.domain.repository.MainRepository

class GetAllRuns(private val mainRepository: MainRepository ) {
    suspend operator fun invoke() = repository.getAllRuns()
}