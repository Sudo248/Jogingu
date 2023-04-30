package com.sudo248.jogingu.ui.activities.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo248.domain.use_case.auth.CheckUserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserLoginUseCase: CheckUserLoginUseCase
) : ViewModel() {

    fun checkUserLogin() = viewModelScope.async(Dispatchers.IO) { checkUserLoginUseCase() }

}