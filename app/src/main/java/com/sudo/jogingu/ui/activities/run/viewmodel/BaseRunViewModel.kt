package com.sudo.jogingu.ui.activities.run.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo.data.util.genId
import com.sudo.domain.entities.Run
import com.sudo.domain.use_case.profile.GetBMRUserUseCase
import com.sudo.domain.use_case.run.AddNewRunUseCase
import com.sudo.jogingu.common.Constant
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.service.BaseRunService
import com.sudo.jogingu.util.toByteArray
import com.sudo.jogingu.util.toTimeHour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.math.round


abstract class BaseRunViewModel(
    private val addNewRunUseCase: AddNewRunUseCase,
    private val getBMRUserUseCase: GetBMRUserUseCase
) : ViewModel() {

    val runState: StateFlow<RunState> = BaseRunService.runState

    val stepCounter: StateFlow<Int> = BaseRunService.stepCounter

    val runningTime: StateFlow<Int> = BaseRunService.runningTime

    val distance: StateFlow<Double> = BaseRunService.distance

    val avgSpeed = MutableStateFlow(0.0)

    protected val _isSuccessToSaveRun = MutableStateFlow(false)
    val isSuccessToSaveRun: StateFlow<Boolean> = _isSuccessToSaveRun

    lateinit var sendCommandToService: (action: String, serviceClass: Class<*> ) -> Unit
    lateinit var getNameRunByTime: (time: Long) -> String
    protected var startTime: Long = 0

    protected fun save(imageInByteArray: ByteArray?) = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("start to save run")
        addNewRunUseCase(
            Run(
                runId = genId("run"),
                name = getNameRunByTime(startTime),
                distance = round(distance.value).toFloat(),
                avgSpeed = round(avgSpeed.value).toFloat(),
                timeRunning = runningTime.value,
                caloBurned = (getBMRUserUseCase() * distance.value / (runningTime.value.toTimeHour())).toInt(),
                timeStart = Date(startTime),
                location = getAddress(),
                stepCount = stepCounter.value,
                imageInByteArray = imageInByteArray
            )
        )
        _isSuccessToSaveRun.value = true
        Timber.d("Save run success")
    }


    abstract fun drawPolylines()
    abstract fun moveCameraToUserPosition()
    abstract fun getAddress(): String

    abstract fun onStartClick()
    abstract fun onPauseOrResumeClick()
    abstract fun onFinishClick(mapHeight: Int, mapWith: Int)

    abstract fun saveRunToDB(mapHeight: Int, mapWith: Int)




}