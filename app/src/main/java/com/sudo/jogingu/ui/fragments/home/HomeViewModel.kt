package com.sudo.jogingu.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo.domain.entities.Run
import com.sudo.domain.use_case.run.GetAllRunsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import com.sudo.domain.common.Result
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRunsUseCase: GetAllRunsUseCase
) : ViewModel() {

    private val _listRun = MutableLiveData<List<Run>>()
    val listRun: LiveData<List<Run>> = _listRun

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllRunsUseCase().collect {
                if(it is Result.Success){
                    _listRun.postValue(it.data)
                }
            }
        }
    }

}