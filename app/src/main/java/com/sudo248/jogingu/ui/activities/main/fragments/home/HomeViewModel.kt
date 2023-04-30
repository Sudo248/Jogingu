package com.sudo248.jogingu.ui.activities.main.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo248.domain.entities.Run
import com.sudo248.domain.use_case.run.GetAllRunsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import com.sudo248.domain.common.DataState
import com.sudo248.domain.common.UiState
import com.sudo248.domain.entities.User
import com.sudo248.domain.entities.UserRun
import com.sudo248.domain.use_case.profile.GetUserUseCase
import com.sudo248.domain.use_case.run.DeleteRunsUseCase
import com.sudo248.domain.use_case.run.GetAllUserRunUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllUserRunsUseCase: GetAllUserRunUseCase,
    private val deleteRunsUseCase: DeleteRunsUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<UiState>(UiState.IDLE)
    val state: LiveData<UiState> = _state

    private val _listRun = MutableLiveData<List<UserRun>?>(null)
    val listRun: LiveData<List<UserRun>?> = _listRun

    init {
        viewModelScope.launch {
            _state.postValue(UiState.LOADING)
            delay(500)
            withContext(Dispatchers.IO) {
                getAllUserRunsUseCase().collect(FlowCollector {
                    _listRun.postValue(
                        if (it is DataState.Success) {
                            it.data
                        } else listOf()
                    )
                    _state.postValue(UiState.SUCCESS)
                })
            }
        }
    }

}