package com.sudo.jogingu.ui.fragments.statistic.viewmodels

import android.content.ContentValues
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sudo.data.repositories.MainRepositoryImpl
import com.sudo.domain.common.Result
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.RunningDay
import com.sudo.domain.use_case.profile.GetNameUserUseCase
import com.sudo.domain.use_case.profile.GetUserUseCase
import com.sudo.domain.use_case.run.GetAllRunsUseCase
import com.sudo.jogingu.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import java.util.concurrent.Flow
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val getAllRunsUseCase: GetAllRunsUseCase,
    private val getNameUserUseCase: GetNameUserUseCase
) : ViewModel() {
    private var runList = ArrayList<RunningDay>()

    fun getDataStatistic(numberDay: Int): List<RunningDay>{
        return getDataStatistic(numberDay)
    }
    fun getDataStatisticFilter(numberDay: Int): List<RunningDay>{

        for(i in 1..numberDay){
            if(numberDay==7)
                if(i==7){
                    runList.add(RunningDay("CN", i%4))
                }
                else
                    runList.add(RunningDay("T${i+1}", i%4))

            else{
                runList.add(RunningDay("${i}", i%7))
            }
        }
        return runList
    }
    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d(ContentValues.TAG, "getAxisLabel: index $index")
            return if (index < runList.size) {
                runList[index].day
            } else {
                ""
            }
        }
    }
//    fun checkRun(){
//        var cal = Calendar.getInstance()
//        for(i in 1..5){
//            if()
//        }
//    }
    fun getTime(time: Int): String {
        if(time == 7){
             return "This week"//Resources.getSystem().getString(R.string.statistic_content_week)
        }
        else if(time == 24){
            return "Today"
        }
    return return "This month"
    }
    fun getSteps(): Long{
        return 100L
    }
    fun getCalories(): Long{
        return 100L
    }
    fun getDistance(): Long{
        return 100L
    }

    fun getTime(): Long{
        return 100L
    }

}