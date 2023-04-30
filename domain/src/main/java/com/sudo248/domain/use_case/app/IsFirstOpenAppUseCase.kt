package com.sudo248.domain.use_case.app

import com.sudo248.domain.repositories.MainRepository

class IsFirstOpenAppUseCase(
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.isFirstOpenApp()
    suspend operator fun invoke(isFirstOpenApp: Boolean) = repo.isFirstOpenApp(isFirstOpenApp)
}