package com.sudo248.jogingu.ui.activities.run.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo248.data.util.genId
import com.sudo248.domain.entities.Run
import com.sudo248.domain.use_case.profile.GetBMRUserUseCase
import com.sudo248.domain.use_case.run.AddNewRunUseCase
import com.sudo248.jogingu.common.RunState
import com.sudo248.jogingu.service.BaseRunService
import com.sudo248.jogingu.util.toTimeHour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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

    private val _isSuccessToSaveRun = MutableStateFlow(false)
    val isSuccessToSaveRun: StateFlow<Boolean> = _isSuccessToSaveRun

    lateinit var sendCommandToService: (action: String, serviceClass: Class<*> ) -> Unit

    lateinit var getNameRunByTime: (time: Long) -> String

    protected var startTime: Long = 0

    protected fun saveRunWith(imageUrl: String? = null, imageInByteArray: ByteArray? = null ) = viewModelScope.launch(Dispatchers.IO) {
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
                imageInByteArray = imageInByteArray,
                imageUrl = imageUrl
            )
        )
        _isSuccessToSaveRun.value = true
        Timber.d("Save run success")
    }


    abstract fun drawPolylines()
    abstract fun drawStartPosition()
    abstract fun moveCameraToUserPosition()
    abstract suspend fun getAddress(): String

    abstract fun onStartClick()
    abstract fun onPauseOrResumeClick()
    abstract fun onFinishClick(mapWidth: Int)

    abstract fun saveRunToDB()

}