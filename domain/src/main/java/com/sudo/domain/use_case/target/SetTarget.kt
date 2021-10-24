package com.sudo.domain.use_case.target

import com.sudo.domain.repositories.MainRepository
import com.sudo.domain.entities.Target

class SetTarget(private val repo: MainRepository) {
    suspend operator fun invoke(target: Target){
        repo.setTarget(target)
    }
}