package com.sudo248.jogingu.ui.activities.main.fragments.target

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo248.domain.common.DataState
import com.sudo248.domain.use_case.target.GetTargetUseCase
import com.sudo248.domain.use_case.target.SetTargetUseCase
import com.sudo248.domain.entities.Target
import com.sudo248.domain.use_case.target.DeleteTargetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class TargetViewModel @Inject constructor(
    private val setTargetUseCase: SetTargetUseCase,
    private val getTargetUseCase: GetTargetUseCase,
    private val deleteTargetUseCase: DeleteTargetUseCase
) : ViewModel() {

    private val _target = MutableLiveData<Target>()
    val target: LiveData<Target> = _target

    private val _saveOrUpdate = MutableLiveData<String>()
    val saveOrUpdate: LiveData<String> = _saveOrUpdate

    fun saveTarget(target: Target){
        viewModelScope.launch(Dispatchers.IO){
            setTargetUseCase(target)
            _target.postValue(target)
        }
    }

    fun deleteTarget(){
        viewModelScope.launch(Dispatchers.IO){
            deleteTargetUseCase()
            _saveOrUpdate.postValue("Save")
        }
    }

    init {
        Timber.d("Init Target Viewmodel")
        viewModelScope.launch(Dispatchers.IO) {
            getTargetUseCase().collect(FlowCollector {
                if(it is DataState.Success){
                    val target = it.data
                    _target.postValue(target)
                }
            })
        }
    }

}