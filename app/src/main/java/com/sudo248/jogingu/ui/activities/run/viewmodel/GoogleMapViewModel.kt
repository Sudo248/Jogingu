package com.sudo248.jogingu.ui.activities.run.viewmodel

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.sudo248.domain.use_case.profile.GetBMRUserUseCase
import com.sudo248.domain.use_case.run.AddNewRunUseCase
import com.sudo248.jogingu.common.Constant
import com.sudo248.jogingu.common.Constant.ACTION_FINISH
import com.sudo248.jogingu.common.Constant.ACTION_PAUSE
import com.sudo248.jogingu.common.Constant.ACTION_RUNNING
import com.sudo248.jogingu.common.Constant.ACTION_START
import com.sudo248.jogingu.common.Polyline
import com.sudo248.jogingu.common.RunState
import com.sudo248.jogingu.service.GoogleMapService
import com.sudo248.jogingu.util.UrlUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
    private var isStart = false

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl

    fun setMap(map: GoogleMap) {
        this.map = map
        sendCommandToService(ACTION_START)
    }

    private fun drawPolylineBetween(preLatLng: LatLng, latLng: LatLng) {
        val polyLineOptions = PolylineOptions()
            .color(Constant.POLYLINE_COLOR)
            .width(Constant.POLYLINE_WIDTH_DEFAULT)
            .add(preLatLng)
            .add(latLng)
        map?.addPolyline(polyLineOptions)
    }

    override fun drawPolylines() {
        if (pathPoints.size > 1) {
            val preLastLatLng = pathPoints[pathPoints.size - 2]
            val lastLatLng = pathPoints.last()
            drawPolylineBetween(preLastLatLng, lastLatLng)
        }
    }

    override fun drawStartPosition() {
        val start = pathPoints.first()
        map?.let {
            val circleOptions = CircleOptions().apply {
                center(start)
                radius(1.75)
                strokeColor(Constant.POLYLINE_COLOR)
                strokeWidth(5.0f)
                fillColor(Color.parseColor("#0352fc"))
            }
            it.addCircle(circleOptions)
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

    override suspend fun getAddress(): String = suspendCoroutine { continuation ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                pathPoints[0].latitude,
                pathPoints[0].longitude,
                1
            ) {
                continuation.resume(it[0].getAddressLine(0))
            }
        } else {
            val listAddress = geocoder.getFromLocation(
                pathPoints[0].latitude,
                pathPoints[0].longitude,
                1
            )
            continuation.resume(listAddress?.get(0)?.getAddressLine(0) ?: "Unknown")
        }
    }

    private fun sendCommandToService(action: String) {
        super.sendCommandToService(action, GoogleMapService::class.java)
    }

    override fun onStartClick() {
        startTime = System.currentTimeMillis()
        sendCommandToService(ACTION_RUNNING)
    }

    override fun onPauseOrResumeClick() {
        if (runState.value == RunState.RUNNING) {
            currentPos = pathPoints.size
            sendCommandToService(ACTION_PAUSE)
        } else {
            continueRun()
        }
    }

    override fun onFinishClick(mapWidth: Int) {
        getMapBoxImageUrl(mapWidth)
    }

    @SuppressLint("MissingPermission")
    override fun saveRunToDB() {
        sendCommandToService(ACTION_FINISH)
        viewModelScope.launch(Dispatchers.Default) {
            saveRunWith(imageUrl = _imageUrl.value)
        }
    }

    fun continueRun() {
        if (runState.value == RunState.PAUSE) {
            if (pathPoints.size > 1) {
                while (currentPos < pathPoints.size) {
                    val preLastLatLng = pathPoints[currentPos - 1]
                    val lastLatLng = pathPoints[currentPos]
                    drawPolylineBetween(preLastLatLng, lastLatLng)
                    currentPos++
                }
            }
            sendCommandToService(ACTION_RUNNING)
        }
    }

    private fun getMapBoxImageUrl(mapWidth: Int) {
        viewModelScope.async(Dispatchers.Default) {
            val bounds = LatLngBounds.Builder()
            var minLat = Double.MAX_VALUE
            var maxLat = Double.MIN_VALUE
            var minLng = Double.MAX_VALUE
            var maxLng = Double.MIN_VALUE
            for (position in pathPoints) {
                val lat = position.latitude
                val lng = position.longitude
                if (maxLat < lat) maxLat = lat
                if (minLat > lat) minLat = lat
                if (maxLng < lng) maxLng = lng
                if (minLng > lng) minLng = lng
                bounds.include(position)
            }
            val height = (mapWidth * 2.0f/3).toInt()
            val encodePolyline = PolyUtil.encode(pathPoints)
            val url = UrlUtils.genStaticMapboxUrl(
                min = LatLng(minLat, minLng),
                max = LatLng(maxLat, maxLng),
                size = Size(mapWidth, height),
                encodePolyline
            )
            Timber.d("Sudoo: saveRunToDB: $url")
            _imageUrl.emit(url)
        }
    }

    init {
        viewModelScope.launch {
            GoogleMapService.latestPoint.collect {
                pathPoints.add(it)
                moveCameraToUserPosition()
                if (runState.value == RunState.RUNNING) {
                    if (!isStart) {
                        val tmp = pathPoints[pathPoints.size - 2]
                        pathPoints.clear()
                        pathPoints.addAll(listOf(tmp, it))
                        drawStartPosition()
                        isStart = true
                    }
                    drawPolylines()
                }
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
//              delay 15s for each calculate avg speed
                delay(15000)
                if (runningTime.value > 0) {
//                    Timber.d("Avg Speed: ${distance.value} / ${runningTime.value} = ${distance.value / runningTime.value}")
                    avgSpeed.value = distance.value / runningTime.value
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        map = null
        pathPoints.clear()
    }

}