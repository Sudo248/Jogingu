package com.sudo248.domain.use_case.profile

import com.sudo248.domain.entities.User
import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.repositories.UserRepository

class SetUserUseCase(
    private val repo: UserRepository
) {
    suspend operator fun invoke(user: User){
        repo.setUser(user)
    }
}