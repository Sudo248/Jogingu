package com.sudo248.jogingu.ui.activities.main.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo248.domain.common.DataState
import com.sudo248.domain.common.UiState
import com.sudo248.domain.entities.User
import com.sudo248.domain.use_case.profile.GetUserUseCase
import com.sudo248.domain.use_case.profile.SetUserUseCase
import com.sudo248.domain.use_case.profile.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val setUserUseCase: SetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _state = MutableLiveData(UiState.IDLE)
    val state: LiveData<UiState> = _state

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _enableSaveOrUpdateButton = MutableLiveData(false)
    val enableSaveOrUpdateButton: LiveData<Boolean> = _enableSaveOrUpdateButton

    private val _isUpdate = MutableLiveData(false)
    val isUpdate: LiveData<Boolean> = _isUpdate

    var isUpdateImage = false

    init{
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(UiState.LOADING)
            getUserUseCase().collect(FlowCollector {
                if(it is DataState.Success){
                    val mUser = it.data
                    if(mUser.userId != null){
                        _user.postValue(mUser)
                        _isUpdate.postValue(true)
                    }
                    _state.postValue(UiState.SUCCESS)
                } else {
                    _state.postValue(UiState.ERROR)
                }
            })
        }
    }

    fun onSaveClick(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(UiState.LOADING)
            _user.postValue(user)
            if (_isUpdate.value == true) {
                updateUserUseCase(user, isUpdateImage)
            } else {
                setUserUseCase(user)
            }
            _state.postValue(UiState.SUCCESS)
        }
    }

    fun onFirstNameChanged(text: String) {
        if (text != _user.value?.firstName) {
            _enableSaveOrUpdateButton.postValue(true)
        }
    }

    fun onLastNameChanged(text: String) {
        if (text != _user.value?.lastName) {
            _enableSaveOrUpdateButton.postValue(true)
        }
    }

    fun onCityChanged(text: String) {
        if (text != _user.value?.city) {
            _enableSaveOrUpdateButton.postValue(true)
        }
    }

}