package com.sudo.jogingu.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo.domain.common.Result
import com.sudo.domain.entities.User
import com.sudo.domain.use_case.profile.GetUserUseCase
import com.sudo.domain.use_case.profile.SetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val setUserUseCase: SetUserUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _saveOrUpdate = MutableLiveData<String>()
    val saveOrUpdate: LiveData<String> = _saveOrUpdate

    init{
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase().collect {
                if(it is Result.Success){
                    val mUser = it.data
                    if(mUser.userId == null){
                        _saveOrUpdate.postValue("Save")
                    }else{
                        _user.postValue(mUser)
                        _saveOrUpdate.postValue("Update")
                    }
                }
            }
        }
    }

    fun onSaveClick(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            setUserUseCase(user)
        }
    }

}