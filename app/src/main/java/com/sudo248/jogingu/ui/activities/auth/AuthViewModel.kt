package com.sudo248.jogingu.ui.activities.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.utils.Utils
import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Account
import com.sudo248.domain.ktx.launchHandler
import com.sudo248.domain.use_case.app.IsFirstOpenAppUseCase
import com.sudo248.domain.use_case.auth.SignInUserCase
import com.sudo248.domain.use_case.auth.SignUpUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val isFirstOpenAppUseCase: IsFirstOpenAppUseCase,
    private val signInUseCase: SignInUserCase,
    private val signUpUseCase: SignUpUserCase,
) : ViewModel() {
    var email: String = ""

    var password: String  = ""

    var confirmPassword: String = ""
        set(value) {
            field = value
            comparePassword()
        }

    private var _passwordsIsEqual: MutableLiveData<Boolean> = MutableLiveData(true)
    val passwordsIsEqual: LiveData<Boolean> = _passwordsIsEqual

    private var _result: MutableLiveData<DataState<String>> = MutableLiveData()
    val result: LiveData<DataState<String>> = _result

    private fun comparePassword() {
        _passwordsIsEqual.postValue(confirmPassword == password)
    }

    fun login() {
        if (email.isBlank() || password.isBlank()) {
            _result.postValue(DataState.Error("Not empty email or password"))
        } else {
            viewModelScope.launchHandler(
                Dispatchers.IO,
                handleException = { _, throwable ->
                    _result.postValue(DataState.Error("${throwable.message}"))
                }
            ) {
                _result.postValue(DataState.Loading)
                _result.postValue(signInUseCase(Account(email = email, password = password))!!)
            }
        }
    }

    fun signUp() {
        viewModelScope.launchHandler(
            Dispatchers.IO,
            handleException = { _, throwable ->
                _result.postValue(DataState.Error("${throwable.message}"))
            }
        ) {
            _result.postValue(DataState.Loading)
            _result.postValue(signUpUseCase(Account(email = email, password = password))!!)
        }
    }

    fun isFirstOpenApp() = viewModelScope.async { isFirstOpenAppUseCase() }
    fun setIsFirstOpenApp(isFirstOpenApp: Boolean) = viewModelScope.async { isFirstOpenAppUseCase(isFirstOpenApp) }
}