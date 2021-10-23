package com.sudo.domain.use_case.profile

import com.sudo.domain.entities.User
import com.sudo.domain.repository.MainRepository

class SetUser(private val repo: MainRepository) {
    suspend operator fun invoke(user: User){
        repo.setUser(user)
    }
}