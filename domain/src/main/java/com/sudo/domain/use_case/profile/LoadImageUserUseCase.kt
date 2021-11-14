package com.sudo.domain.use_case.profile

import com.sudo.domain.repositories.MainRepository

class LoadImageUserUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke(pathImage: String) = repo.loadImageFromDevice(pathImage)
}