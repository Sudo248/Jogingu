package com.sudo.domain.use_case.profile

import com.sudo.domain.repository.MainRepository

class GetUser(private val repo: MainRepository) {
    suspend operator fun invoke() = repo.getUser()
}