package com.sudo248.jogingu.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant.ACTION_FINISH
import com.sudo248.jogingu.common.Constant.ACTION_PAUSE
import com.sudo248.jogingu.common.Constant.ACTION_RUNNING
import com.sudo248.jogingu.common.Constant.ACTION_START
import com.sudo248.jogingu.common.Constant.NOTIFICATION_RUNNING_CHANNEL_ID
import com.sudo248.jogingu.common.Constant.NOTIFICATION_RUNNING_CHANNEL_NAME
import com.sudo248.jogingu.common.Constant.NOTIFICATION_RUNNING_ID
import com.sudo248.jogingu.common.RunState
import com.sudo248.jogingu.util.TimeUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.TimerTask
import java.util.Timer
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
abstract class BaseRunService : LifecycleService(), SensorEventListener {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat
    @Inject
    @Named("RunningNotificationBuilder")
    lateinit var notificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var sensorManager: SensorManager
    @Inject
    @Named("PauseService") lateinit var pendingIntentPauseService: PendingIntent
    @Inject
    @Named("RunningService") lateinit var pendingIntentRunningService: PendingIntent

    private lateinit var sensor: Sensor
    private lateinit var currentNotificationBuilder: NotificationCompat.Builder

    private val timer = Timer()
    private lateinit var timerTask: TimerTask
    private var magnitudePrevious = 0.0
    private var isServiceKill = false

    companion object{
        val runningTime = MutableStateFlow(0)
        val stepCounter = MutableStateFlow(0)
        val runState = MutableStateFlow(RunState.START)
        val distance = MutableStateFlow(0.0)
    }

    override fun onCreate() {
        super.onCreate()
        currentNotificationBuilder = notificationBuilder

        lifecycleScope.launchWhenCreated {
            runState.collect {
                withContext(Dispatchers.Default){
                    updateRunState(it)
                    updateNotification(it)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(it.action){
                ACTION_START -> {
                    Timber.d("Start")
                    runState.value = RunState.START
                }
                ACTION_RUNNING -> {
                    Timber.d("Running")
                    runState.value = RunState.RUNNING
                }
                ACTION_PAUSE -> {
                    Timber.d("Pause")
                    runState.value = RunState.PAUSE

                }
                ACTION_FINISH -> {
                    Timber.d("Finish")
                    runState.value = RunState.FINISH
                }
                else -> {
                    Timber.d("Stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateRunState(state: RunState){
        when(state){
            RunState.START -> {
                registerUpdatePosition()
                registerSensorListener()
            }
            RunState.RUNNING -> {
                startTimeCounter()
                startForegroundService()
            }
            RunState.PAUSE -> {
                cancelTimeCounter()
            }
            RunState.FINISH -> {
                unregisterUpdatePosition()
                unregisterSensorListener()
                isServiceKill = true
                stopForeground(true)
                stopSelf()
            }
        }
    }

    private fun startForegroundService(){
        startForeground(NOTIFICATION_RUNNING_ID, notificationBuilder.build())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                runningTime.collect {
                    withContext(Dispatchers.Default){
//                    Timber.d("time running: $it")
                        val notification = currentNotificationBuilder
                            .setContentTitle(TimeUtil.parseTime(it) + " - "+"%.2f km".format(distance.value/1000))
                        notificationManager.notify(NOTIFICATION_RUNNING_ID, notification.build())
                    }
                }
            }
        }
    }

    private fun updateNotification(state: RunState){

        var notificationActionText = "Pause"
        var pendingIntent = pendingIntentPauseService
        if(state == RunState.PAUSE){
            notificationActionText = "Resume"
            pendingIntent = pendingIntentRunningService
        }
        // remove all action
        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        if(!isServiceKill){
            currentNotificationBuilder = notificationBuilder
                .addAction(R.drawable.ic_pause_24, notificationActionText, pendingIntent)

            notificationManager.notify(NOTIFICATION_RUNNING_ID, currentNotificationBuilder.build())
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        lifecycleScope.launch(Dispatchers.Default){
            if (runState.value == RunState.RUNNING && event != null) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
                val magnitudeDelta = magnitude - magnitudePrevious
                magnitudePrevious = magnitude.toDouble()
                if (magnitudeDelta > 6.5) {
                    stepCounter.value++
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun startTimeCounter(){
        timerTask = object : TimerTask(){
            override fun run() {
                runningTime.value++
            }
        }
        timer.scheduleAtFixedRate(timerTask,0,1000)
    }

    private fun cancelTimeCounter(){
        timerTask.cancel()
    }

    private fun registerSensorListener(){
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun unregisterSensorListener(){
        sensorManager.unregisterListener(this)
    }

    abstract fun registerUpdatePosition()
    abstract fun unregisterUpdatePosition()

}