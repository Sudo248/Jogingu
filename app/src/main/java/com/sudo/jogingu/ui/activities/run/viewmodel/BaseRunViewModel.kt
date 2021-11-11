package com.sudo.jogingu.ui.activities.run.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.sudo.jogingu.common.Constant
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.service.BaseRunService
import com.sudo.jogingu.util.getDirApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

abstract class BaseRunViewModel : ViewModel() {

    val runState: StateFlow<RunState> = BaseRunService.runState

    val stepCounter: StateFlow<Int> = BaseRunService.stepCounter

    val runningTime: StateFlow<Int> = BaseRunService.runningTime

    val distance: StateFlow<Double> = BaseRunService.distance

    val avgSpeed = MutableStateFlow(0.0)

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

    abstract fun saveRunToDB(mapHeight: Int, mapWith: Int)


}