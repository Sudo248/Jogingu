package com.sudo.jogingu.ui.fragments.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo.domain.common.Result
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.RunInStatistic
import com.sudo.domain.use_case.profile.GetNameUserUseCase
import com.sudo.domain.use_case.statistic.GetRunsThisDayUseCase
import com.sudo.domain.use_case.statistic.GetRunsThisMonthUseCase
import com.sudo.domain.use_case.statistic.GetRunsThisWeekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val getRunsThisDayUseCase: GetRunsThisDayUseCase,
    private val getRunsThisWeekUseCase: GetRunsThisWeekUseCase,
    private val getRunsThisMonthUseCase: GetRunsThisMonthUseCase,
    private val getNameUserUseCase: GetNameUserUseCase
) : ViewModel() {

    private val _listRun = MutableLiveData<List<RunInStatistic>>()
    val listRun: LiveData<List<RunInStatistic>> = _listRun

    var totalStep: Int = 0
    var totalCaloBurned: Float = 0.0f
    var totalTimeRunning: Int = 0
    var totalDistance: Float = 0.0f

    private var listRunThisDay: List<RunInStatistic>? = null
    private var listRunThisWeek: List<RunInStatistic>? = null
    private var listRunThisMonth: List<RunInStatistic>? = null

    init {
//        getRunsThisWeek()
        test()
    }

    fun getRunsThisDay(){
        if(listRunThisDay == null){
            viewModelScope.launch(Dispatchers.IO) {
                getRunsThisDayUseCase().collect {
                    if(it is Result.Success){
                        listRunThisDay = it.data
                        _listRun.postValue(listRunThisDay)
                    }
                }
            }
        }else{
            _listRun.postValue(listRunThisDay)
        }
        listRunThisDay?.let{ calculateTotal(it) }
    }

    fun getRunsThisWeek(){
        if(listRunThisWeek == null){
            viewModelScope.launch(Dispatchers.IO) {
                getRunsThisWeekUseCase().collect {
                    if(it is Result.Success){
                        listRunThisWeek = it.data
                        _listRun.postValue(listRunThisWeek)
                    }
                }
            }
        }else{
            _listRun.postValue(listRunThisWeek)
        }

        listRunThisWeek?.let{ calculateTotal(it) }
    }

    fun getRunsThisMonth(){
        if(listRunThisMonth == null){
            viewModelScope.launch(Dispatchers.IO) {
                getRunsThisMonthUseCase().collect {
                    if(it is Result.Success){
                        listRunThisMonth = it.data
                        _listRun.postValue(listRunThisMonth)
                    }
                }
            }
        }else{
            _listRun.postValue(listRunThisMonth)
        }
        listRunThisMonth?.let{ calculateTotal(it) }
    }

    private fun calculateTotal(listRun: List<RunInStatistic>){
        totalStep = 0
        totalCaloBurned = 0.0f
        totalTimeRunning = 0
        totalDistance = 0.0f
        for (run in listRun){
            totalStep += run.stepCount
            totalCaloBurned += run.caloBurned
            totalTimeRunning += run.timeRunning
            totalDistance += run.distance
        }
    }

    fun test(){
        val runTmp = RunInStatistic(
            "duong",
            12,
            100,
            Date(System.currentTimeMillis()),
            5.0f,
            1000,
        )
        val listTmp = mutableListOf<RunInStatistic>(runTmp)
        for (i in 1..6){
            listTmp.add(runTmp.copy(distance = i*1.0f))
        }
        listRunThisWeek = listTmp
        getRunsThisWeek()
    }

}