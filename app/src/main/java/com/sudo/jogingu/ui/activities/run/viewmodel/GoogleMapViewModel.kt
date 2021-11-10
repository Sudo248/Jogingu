package com.sudo.jogingu.ui.activities.run.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.sudo.jogingu.common.Constant
import com.sudo.jogingu.common.Constant.ACTION_FINISH
import com.sudo.jogingu.common.Constant.ACTION_PAUSE
import com.sudo.jogingu.common.Constant.ACTION_RUNNING
import com.sudo.jogingu.common.Constant.ACTION_START
import com.sudo.jogingu.common.Polyline
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.service.GoogleMapService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class GoogleMapViewModel : BaseRunViewModel() {

    private var map: GoogleMap? = null
    private var pathPoints: Polyline = mutableListOf()
    lateinit var context: Context


    private val jobUpdatePolyline = Job()
    private val jobUpdatePosition = Job()

    fun setMap(map: GoogleMap){
        this.map = map
        this.map!!.moveCamera(CameraUpdateFactory.zoomTo(Constant.MAP_ZOOM_DEFAULT))
        sendCommandToService(ACTION_START)
    }

    override fun drawPolylines() {
        if(pathPoints.size > 1){
            val preLastLatLng = pathPoints[pathPoints.size-2]
            val lastLatLng = pathPoints.last()
            val polyLineOptions = PolylineOptions()
                .color(Constant.POLYLINE_COLOR)
                .width(Constant.POLYLINE_WIDTH_DEFAULT)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polyLineOptions)
        }
    }

    override fun moveCameraToUserPosition() {
//        val cameraPosition = if(firstOpenMap) {
//            firstOpenMap = false
//            MAP_ZOOM_DEFAULT
//        } else map.cameraPosition.zoom

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
        sendCommandToService(ACTION_RUNNING)
    }

    override fun onPauseOrResumeClick(){
        if(runState.value == RunState.RUNNING){
            sendCommandToService(ACTION_PAUSE)
        }else{
            sendCommandToService(ACTION_RUNNING)
        }
    }

    override fun onFinishClick(){
        sendCommandToService(ACTION_FINISH)
    }

    init {
        viewModelScope.launch {
            GoogleMapService.pathPoints.collect {
                pathPoints = it
                moveCameraToUserPosition()
                if(runState.value == RunState.RUNNING){
                    drawPolylines()
                }
            }
        }
    }

}