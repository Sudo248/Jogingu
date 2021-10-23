package com.sudo.domain.use_case.target

import com.sudo.domain.repository.MainRepository
import com.sudo.domain.entities.Target

class SetTarget(private val repo: MainRepository) {
    suspend operator fun invoke(target: Target){
        repo.setTarget(target)
    }
}