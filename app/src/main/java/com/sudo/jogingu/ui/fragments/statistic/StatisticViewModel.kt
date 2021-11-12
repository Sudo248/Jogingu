package com.sudo.jogingu.ui.fragments.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo.domain.entities.Run
import com.sudo.domain.use_case.profile.GetNameUserUseCase
import com.sudo.domain.use_case.run.GetAllRunsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val getAllRunsUseCase: GetAllRunsUseCase,
    private val getNameUserUseCase: GetNameUserUseCase
) : ViewModel() {

    private val _listRun = MutableLiveData<Run>()
    val listRun: LiveData<Run> = _listRun

}