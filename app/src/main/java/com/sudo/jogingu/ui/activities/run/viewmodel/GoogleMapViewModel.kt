package com.sudo.jogingu.ui.activities.run.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Geocoder
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.sudo.jogingu.util.toByteArray
import com.sudo.domain.use_case.profile.GetBMRUserUseCase
import com.sudo.domain.use_case.run.AddNewRunUseCase
import com.sudo.jogingu.common.Constant
import com.sudo.jogingu.common.Constant.ACTION_FINISH
import com.sudo.jogingu.common.Constant.ACTION_PAUSE
import com.sudo.jogingu.common.Constant.ACTION_RUNNING
import com.sudo.jogingu.common.Constant.ACTION_START
import com.sudo.jogingu.common.Polyline
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.service.GoogleMapService
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GoogleMapViewModel @Inject constructor(
    addNewRunUseCase: AddNewRunUseCase,
    getBMRUserUseCase: GetBMRUserUseCase,
    private val geocoder: Geocoder
) : BaseRunViewModel(
    addNewRunUseCase,
    getBMRUserUseCase
) {

    private var map: GoogleMap? = null
    private var pathPoints: Polyline = mutableListOf()
    private var currentPos = 0

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

    override fun getAddress(): String {
        val listAddress = geocoder.getFromLocation(pathPoints[0].latitude, pathPoints[0].longitude,1)
//        return listAddress[0].getAddressLine(0)
        return "Yet Kieu - Ha Dong"
    }

    private fun sendCommandToService(action: String){
        super.sendCommandToService(action, GoogleMapService::class.java)
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

    override fun onFinishClick(mapHeight: Int, mapWith: Int){
        sendCommandToService(ACTION_FINISH)
        saveRunToDB(mapHeight, mapWith)
    }

    override fun saveRunToDB(mapHeight: Int, mapWith: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            zoomToSeeWholeTrack(mapHeight, mapWith)
            map?.snapshot { bmp ->
                save(bmp?.toByteArray())
                _isSuccessToSaveRun.value = true
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun zoomToSeeWholeTrack(mapHeight: Int, mapWith: Int){
        val bounds = LatLngBounds.Builder()
        for(position in pathPoints){
            bounds.include(position)
        }
        withContext(Dispatchers.Main){
            Timber.d("bounds: $bounds")
            map?.isMyLocationEnabled = false
            map?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds.build(),
                    mapWith,
                    mapHeight / 3,
                    (mapHeight * 0.05f).toInt()
                )
            )
        }
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
//                    Timber.d("Avg Speed: ${distance.value} / ${runningTime.value} = ${distance.value / runningTime.value}")
                    avgSpeed.value = distance.value / runningTime.value
                }
            }
        }
    }

}