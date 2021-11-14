package com.sudo.domain.use_case.profile

import com.sudo.domain.entities.User
import com.sudo.domain.repositories.MainRepository

class UpdateUserUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke(user: User) {
        repo.updateUser(user)
    }
}