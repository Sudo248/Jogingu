package com.sudo.domain.use_case.profile

import com.sudo.domain.repositories.MainRepository

class GetUser(private val repo: MainRepository) {
    suspend operator fun invoke() = repo.getUser()
}