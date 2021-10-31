package com.sudo.jogingu.ui.activities.run

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.sudo.jogingu.R
import com.sudo.jogingu.common.Constant.ACTION_FINISH
import com.sudo.jogingu.common.Constant.ACTION_PAUSE
import com.sudo.jogingu.common.Constant.ACTION_RUNNING
import com.sudo.jogingu.common.Constant.ACTION_START
import com.sudo.jogingu.common.Constant.LATITUDE_DEFAULT
import com.sudo.jogingu.common.Constant.LONGITUDE_DEFAULT
import com.sudo.jogingu.common.Constant.MAP_ZOOM_DEFAULT
import com.sudo.jogingu.common.Constant.POLYLINE_COLOR
import com.sudo.jogingu.common.Constant.POLYLINE_WIDTH_DEFAULT
import com.sudo.jogingu.common.Polylines
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.databinding.ActivityRunBinding
import com.sudo.jogingu.service.RunningService
import com.sudo.jogingu.util.TrackingPermission
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class RunActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityRunBinding
    private lateinit var map: GoogleMap
    private var runState: RunState = RunState.START
    private var pathPoints: Polylines = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // check permission for location
        TrackingPermission.requestPermission(this)

        binding.mapView.onCreate(savedInstanceState)

        getMapAsync()
        subscribeUi()
        subscribeObserver()

    }


    @SuppressLint("MissingPermission")
    private fun getMapAsync(){
        if(TrackingPermission.hasLocationPermissions(this)){
            binding.mapView.getMapAsync {
                map = it
                map.isMyLocationEnabled = true
                addAllPolyline()
                if(runState == RunState.START){
                    sendCommandToService(ACTION_START)
                }
            }
        }
    }

    private fun subscribeUi(){
        binding.fabStart.setOnClickListener {
            binding.glStartRunMain.setGuidelinePercent(0.6F)
            binding.ctlBeforeStart.visibility = View.GONE
            binding.ctlAfterStart.visibility = View.VISIBLE
            sendCommandToService(ACTION_RUNNING)
        }

        binding.fabResumeContinue.setOnClickListener {
            if(runState == RunState.RUNNING){
                sendCommandToService(ACTION_PAUSE)
            }else{
                sendCommandToService(ACTION_RUNNING)
            }
        }

        binding.fabFinish.setOnClickListener {
            sendCommandToService(ACTION_FINISH)
        }

    }

    private fun subscribeObserver(){

        RunningService.runState.observe(this){
            runState = it
            updateUi()
        }

        RunningService.pathPoints.observe(this){
            pathPoints = it
            if(runState == RunState.RUNNING){
                drawLatestPolyLine()
                moveCameraToUser()
            }else{
                showMyPosition()
                moveCameraToUser(MAP_ZOOM_DEFAULT)
            }
        }

    }

    private fun sendCommandToService(action: String){
        Intent(this, RunningService::class.java).also {
            it.action = action
            this.startService(it)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateUi() {
        when(runState){
            RunState.RUNNING -> {
                binding.fabFinish.visibility = View.GONE
                binding.fabResumeContinue.icon = getDrawable(R.drawable.ic_pause_24)
            }
            RunState.PAUSE -> {
                binding.fabResumeContinue.icon = getDrawable(R.drawable.ic_play_arrow_24)
                binding.fabFinish.visibility = View.VISIBLE
            }
            RunState.FINISH -> {

            }
            else -> {

            }
        }
    }

    private fun showMyPosition(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            val lastLatLng = pathPoints.last().last()
            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH_DEFAULT)
                .add(lastLatLng)
            map.addPolyline(polyLineOptions)
        }
    }

    private fun drawLatestPolyLine(){
        if(pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size-2]
            val lastLatLng = pathPoints.last().last()
            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH_DEFAULT)
                .add(preLastLatLng)
                .add(lastLatLng)
            map.addPolyline(polyLineOptions)
        }
    }

    private fun moveCameraToUser(cameraPosition: Float? = null){
        Timber.d("camera position: $cameraPosition")
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    cameraPosition ?: map.cameraPosition.zoom
                )
            )
        }
    }

    // use when rotate screen
    private fun addAllPolyline(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH_DEFAULT)
                .addAll(polyline)
            map.addPolyline(polylineOptions)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TrackingPermission.onPermissionsDenied(this, perms)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        TrackingPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getMapAsync()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // for mapview sync activity lifecycle
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}