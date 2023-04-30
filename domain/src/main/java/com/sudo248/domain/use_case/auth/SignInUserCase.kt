package com.sudo248.domain.use_case.auth

import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Account
import com.sudo248.domain.repositories.AuthRepository
import kotlinx.coroutines.coroutineScope

class SignInUserCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(account: Account): DataState<String> = coroutineScope {
        if (account.email.isBlank() || account.password.isBlank()) {
            DataState.Error("Not empty email or password")
        }
        repo.login(account)
    }
}