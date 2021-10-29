package com.sudo.jogingu.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.sudo.jogingu.common.Constant.ACTION_FINISH
import com.sudo.jogingu.common.Constant.ACTION_PAUSE
import com.sudo.jogingu.common.Constant.ACTION_RUNNING
import com.sudo.jogingu.common.Constant.ACTION_START
import com.sudo.jogingu.common.Constant.FASTEST_LOCATION_INTERVAL
import com.sudo.jogingu.common.Constant.LOCATION_UPDATE_INTERVAL
import com.sudo.jogingu.common.Constant.NOTIFICATION_CHANNEL_ID
import com.sudo.jogingu.common.Constant.NOTIFICATION_CHANNEL_NAME
import com.sudo.jogingu.common.Constant.NOTIFICATION_ID
import com.sudo.jogingu.common.Polylines
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.helper.GeneralHelper
import com.sudo.jogingu.util.TrackingPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

import javax.inject.Inject

@AndroidEntryPoint
class RunningService : LifecycleService(), SensorEventListener {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var sensorManager: SensorManager

    private lateinit var sensor: Sensor

    private val timer = Timer()
    private lateinit var timerTask: TimerTask
    private var startTime = 0L
    private var runningTimeInSeconds = 0L
    private var magnitudePrevious = 0.0
    private var stepCounts = 0

    companion object{
        val runningTime = MutableLiveData<Long>()
        val stepCounter = MutableLiveData<Int>()
        val runState = MutableLiveData<RunState>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    override fun onCreate() {
        super.onCreate()
        // init
        pathPoints.postValue(mutableListOf(mutableListOf()))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(it.action){
                ACTION_START -> {
                    Timber.d("Start")
                    runState.postValue(RunState.START)
                    registerUpdatePosition()
                    registerSensorListener()
                }
                ACTION_RUNNING -> {
                    Timber.d("Running")
                    if(runState.value == RunState.START){
                        startTime = System.currentTimeMillis()
                    }
                    runState.postValue(RunState.RUNNING)
                    startTimeCounter()
                    startForegroundService()
                }
                ACTION_PAUSE -> {
                    Timber.d("Pause")
                    runState.postValue(RunState.PAUSE)
                    cancelTimeCounter()
                }
                ACTION_FINISH -> {
                    Timber.d("Finish")
                    runState.postValue(RunState.FINISH)
                    unregisterUpdatePosition()
                    unregisterSensorListener()
                    TODO("snapshot and save running")
                }
                else -> {
                    Timber.d("Stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimeCounter(){
        timerTask = object : TimerTask(){
            override fun run() {
                runningTimeInSeconds++
                runningTime.postValue(runningTimeInSeconds)
            }
        }
        timer.scheduleAtFixedRate(timerTask,0,1000)
    }

    private fun cancelTimeCounter(){
        timerTask.cancel()
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun registerSensorListener(){
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    @SuppressLint("MissingPermission")
    private fun registerUpdatePosition(){
        if(TrackingPermission.hasLocationPermissions(this)){
            val request = LocationRequest.create().apply {
                interval = LOCATION_UPDATE_INTERVAL
                fastestInterval = FASTEST_LOCATION_INTERVAL
                priority = PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallBack,
                Looper.getMainLooper()
            )
        }
    }

    private fun unregisterUpdatePosition(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
    }

    private fun unregisterSensorListener(){
        sensorManager.unregisterListener(this)
    }

    private val locationCallBack = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.let { locations ->
                for(location in locations){
                    addPathPoint(location)
                }
            }
        }
    }

    private fun addPathPoint(location: Location?){
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun startForegroundService(){
        addEmptyPolyline()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotification()
        }
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        CoroutineScope(Dispatchers.Default).launch {
            if (runState.value == RunState.RUNNING && event != null) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
                val magnitudeDelta = magnitude - magnitudePrevious
                magnitudePrevious = magnitude.toDouble()
                if (magnitudeDelta > 6.5) {
                    stepCounts++
                    stepCounter.postValue(stepCounts)
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

}