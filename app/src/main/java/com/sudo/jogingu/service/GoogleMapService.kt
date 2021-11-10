package com.sudo.jogingu.service

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.sudo.jogingu.common.Constant
import com.sudo.jogingu.common.Constant.LATITUDE_DEFAULT
import com.sudo.jogingu.common.Constant.LONGITUDE_DEFAULT
import com.sudo.jogingu.common.Polyline
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.util.TrackingPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GoogleMapService : BaseRunService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val jobUpdate = Job()

    companion object{
        val pathPoints = MutableStateFlow<Polyline>(mutableListOf())
    }

    private val locationCallBack = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            lifecycleScope.launch(Dispatchers.Default) {
                for(location in result.locations){
                    addPathPoint(location)
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        //Timber.d("Thread in add path: ${Thread.currentThread().name}")
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            val tmp = pathPoints.value.toMutableList()
            if(tmp.size > 1 && runState.value == RunState.RUNNING){
                distance.value += calculateDistance(tmp.last(), pos)
            }
            tmp.add(pos)
            pathPoints.value = tmp
        }
    }

    @SuppressLint("MissingPermission")
    override fun registerUpdatePosition() {
        Timber.d("register Update Position")
        if(TrackingPermission.hasLocationPermissions(this)){
            val request = LocationRequest.create().apply {
                interval = Constant.LOCATION_UPDATE_INTERVAL
                fastestInterval = Constant.FASTEST_LOCATION_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallBack,
                Looper.getMainLooper()
            )

        }
    }

    override fun unregisterUpdatePosition() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
        jobUpdate.cancel()
    }

    private fun calculateDistance(p1: LatLng, p2: LatLng): Float{
//        Timber.d("Thread in calculate: ${Thread.currentThread().name}")
        val result = FloatArray(1)
        Location.distanceBetween(
            p1.latitude,
            p1.longitude,
            p1.latitude,
            p2.longitude,
            result
        )
        return result[0]
    }

}