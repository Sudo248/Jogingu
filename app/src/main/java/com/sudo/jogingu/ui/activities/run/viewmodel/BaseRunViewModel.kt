package com.sudo.jogingu.ui.activities.run.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.service.BaseRunService
import kotlinx.coroutines.flow.StateFlow

abstract class BaseRunViewModel : ViewModel() {

    val runState: StateFlow<RunState> = BaseRunService.runState

    val stepCounter: StateFlow<Int> = BaseRunService.stepCounter

    val runningTime: StateFlow<Long> = BaseRunService.runningTime

    val distance: StateFlow<Double> = BaseRunService.distance

    protected fun sendCommandToService(context: Context, action: String, serviceClass: Class<*> ){
        Intent(context, serviceClass).also {
            it.action = action
            context.startService(it)
        }
    }

    abstract fun drawPolylines()
    abstract fun moveCameraToUserPosition()

    abstract fun onStartClick()
    abstract fun onPauseOrResumeClick()
    abstract fun onFinishClick()

}