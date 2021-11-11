package com.sudo.jogingu.ui.activities.run.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.sudo.data.util.genId
import com.sudo.domain.entities.Run
import com.sudo.domain.use_case.run.AddNewRunUseCase
import com.sudo.jogingu.common.Constant
import com.sudo.jogingu.common.Constant.ACTION_FINISH
import com.sudo.jogingu.common.Constant.ACTION_PAUSE
import com.sudo.jogingu.common.Constant.ACTION_RUNNING
import com.sudo.jogingu.common.Constant.ACTION_START
import com.sudo.jogingu.common.Polyline
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.service.GoogleMapService
import com.sudo.jogingu.util.saveImageToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GoogleMapViewModel @Inject constructor(
    private val addNewRunUseCase: AddNewRunUseCase
) : BaseRunViewModel() {

    private var map: GoogleMap? = null
    private var pathPoints: Polyline = mutableListOf()
    private var startTime: Long = 0
    private var currentPos = 0
    lateinit var context: Context

    fun setMap(map: GoogleMap){
        this.map = map
        sendCommandToService(ACTION_START)
    }

    private fun drawPolylineBetween(preLatLng: LatLng, latLng: LatLng){
        val polyLineOptions = PolylineOptions()
            .color(Constant.POLYLINE_COLOR)
            .width(Constant.POLYLINE_WIDTH_DEFAULT)
            .add(preLatLng)
            .add(latLng)
        map?.addPolyline(polyLineOptions)
    }

    override fun drawPolylines() {
        if(pathPoints.size > 1){
            val preLastLatLng = pathPoints[pathPoints.size-2]
            val lastLatLng = pathPoints.last()
            drawPolylineBetween(preLastLatLng, lastLatLng)
        }
    }

    override fun moveCameraToUserPosition() {
        map?.let {
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last(),
                    it.cameraPosition.zoom
                )
            )
        }
    }

    private fun sendCommandToService(action: String){
        super.sendCommandToService(context, action, GoogleMapService::class.java)
    }

    override fun onStartClick(){
        startTime = System.currentTimeMillis()
        sendCommandToService(ACTION_RUNNING)
    }

    override fun onPauseOrResumeClick(){
        if(runState.value == RunState.RUNNING){
            currentPos = pathPoints.size
//            Timber.d("Pause with current: ${pathPoints.size}")
            sendCommandToService(ACTION_PAUSE)
        }else if(runState.value == RunState.PAUSE){
            if(pathPoints.size > 1){
//                Timber.d("Start with current: $currentPos")
                while (currentPos < pathPoints.size){
                    val preLastLatLng = pathPoints[currentPos-1]
                    val lastLatLng = pathPoints[currentPos]
                    drawPolylineBetween(preLastLatLng, lastLatLng)
                    currentPos++
                }
            }
            sendCommandToService(ACTION_RUNNING)
        }
    }

    override fun onFinishClick(){
        sendCommandToService(ACTION_FINISH)
    }

    override fun saveRunToDB(mapHeight: Int, mapWith: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            zoomToSeeWholeTrack(mapHeight, mapWith)
            var pathImage: Deferred<String>? = null

            map?.snapshot { bmp ->
                pathImage = viewModelScope.async(Dispatchers.IO){
                    saveImageToFile(bmp)
                }
            }

            val newRun = Run(
                runId = genId("run"),
                name = "Today",
                distance = kotlin.math.round(distance.value).toFloat(),
                avgSpeed = kotlin.math.round(avgSpeed.value).toFloat(),
                timeRunning = runningTime.value,
                caloBurned = 10,
                timeStart = Date(startTime),
                location = "",
                imageUrl = pathImage?.await() ?: ""
            )

        }
    }

    private fun zoomToSeeWholeTrack(mapHeight: Int, mapWith: Int){
        val bounds = LatLngBounds.Builder()
        for(position in pathPoints){
            bounds.include(position)
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapWith,
                mapHeight,
                (mapHeight * 0.05f).toInt()
            )
        )

    }

    init {
        viewModelScope.launch {
            GoogleMapService.latestPoint.collect {
                pathPoints.add(it)
                moveCameraToUserPosition()
                if(runState.value == RunState.RUNNING){
                    drawPolylines()
                }
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            while(true){
//              delay 15s for each calculate avg speed
                delay(15000)
                if(runningTime.value > 0){
                    Timber.d("Avg Speed: ${distance.value} / ${runningTime.value} = ${distance.value / runningTime.value}")
                    avgSpeed.value = distance.value / runningTime.value
                }
            }
        }
    }

}