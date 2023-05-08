package com.sudo248.domain.use_case.profile

import com.sudo248.domain.repositories.MainRepository
import com.sudo248.domain.repositories.UserRepository

class LoadImageUserUseCase(
    private val repo: UserRepository
) {
    suspend operator fun invoke(pathImage: String) = repo.loadImageFromDevice(pathImage)
}