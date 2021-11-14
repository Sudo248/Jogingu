package com.sudo.domain.use_case.app

import com.sudo.domain.repositories.MainRepository

class IsFirstOpenApp(
    private val repo: MainRepository
) {
    suspend operator fun invoke() = repo.isFirstOpenApp()
    suspend operator fun invoke(isFirstOpenApp: Boolean) = repo.isFirstOpenApp(isFirstOpenApp)
}