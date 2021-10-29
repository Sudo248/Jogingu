package com.sudo.jogingu.service

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.sudo.jogingu.callback.StepCallback
import com.sudo.jogingu.helper.GeneralHelper
import timber.log.Timber
import kotlin.math.roundToInt

class StepService : Service(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private lateinit var sensor: Sensor
    private var running = true
    private var magnitudePrevious = 0.0
    private var stepCount = 0

    companion object {
        lateinit var callback: StepCallback
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("step service", "on create")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        loadData()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
        GeneralHelper.updateNotification(this, this, sharedPreferences.getInt("primaryKey", 0))
        callback.subscribeSteps(sharedPreferences.getInt("primaryKey", 0))

        return START_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun onResume() {
        running = true
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun onPause() {
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
            val magnitudeDelta = magnitude - magnitudePrevious
            magnitudePrevious = magnitude.toDouble()
            if (magnitudeDelta > 6.5) {
                stepCount++
                saveData()
                val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
                val savedNumber = sharedPreferences.getInt("primaryKey", 0)
                if (savedNumber > 0) {
                    GeneralHelper.updateNotification(this, this, savedNumber)
                    callback.subscribeSteps(savedNumber)
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private fun resetStep() {
        stepCount = 0
        saveData()
    }

    private  fun saveData() {
        val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("primaryKey", stepCount)
        editor.apply()
    }

    private fun loadData() {
        Timber.tag("StepService").d("loadData: in load data")

        val sharedPreferences = getSharedPreferences("myPre", MODE_PRIVATE)
        val savedNumber = sharedPreferences.getInt("primaryKey", 0)
        stepCount = savedNumber
    }

    object Subscribe {
        fun register(activity: Activity) {
            callback = activity as StepCallback
        }
    }
}