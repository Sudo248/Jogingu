package com.sudo.jogingu.ui.activities.run

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.sudo.jogingu.R
import com.sudo.jogingu.common.Constant
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.databinding.ActivityRunBinding
import com.sudo.jogingu.ui.activities.main.MainActivity
import com.sudo.jogingu.ui.activities.run.viewmodel.GoogleMapViewModel
import com.sudo.jogingu.util.TimeUtil
import com.sudo.jogingu.util.TrackingPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

@AndroidEntryPoint
class RunActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityRunBinding
    private val viewModel: GoogleMapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // check permission for location
        TrackingPermission.requestPermission(this)

        binding.mapView.onCreate(savedInstanceState)

        viewModel.sendCommandToService = { action, serviceClass ->
            Intent(this, serviceClass).also {
                it.action = action
                this.startService(it)
            }
        }

        viewModel.getNameRunByTime = {
            TimeUtil.getNameRunByTime(this, it)
        }

        getMapAsync()
        subscribeUi()
        collect()

    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun collect() {
        lifecycleScope.launchWhenStarted {
            viewModel.runState.collect{
                when(it){
                    RunState.RUNNING -> {
                        binding.fabFinish.visibility = View.GONE
                        binding.fabPauseOrResume.icon = getDrawable(R.drawable.ic_pause_24)
                    }
                    RunState.PAUSE -> {
                        binding.fabPauseOrResume.icon = getDrawable(R.drawable.ic_play_arrow_24)
                        binding.fabFinish.visibility = View.VISIBLE
                    }
                    else -> {
                        Timber.d("collect runState: $it")
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.runningTime.collect {
                binding.tvTimeValue.text = TimeUtil.parseTime(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.stepCounter.collect {
                binding.tvStepValue.text = it.toString()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.distance.collect {
                binding.tvDistance.text = "%.2f".format(it/1000)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.avgSpeed.collect {
                binding.tvSpeedValue.text = "%.2f".format(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isSuccessToSaveRun.collect{
                if(it){
                    startMainActivity()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMapAsync(){
        if(TrackingPermission.hasLocationPermissions(this)){
            binding.mapView.getMapAsync {
                it.isMyLocationEnabled = true
                it.moveCamera(CameraUpdateFactory.zoomTo(Constant.MAP_ZOOM_DEFAULT))
                viewModel.setMap(it)
            }
        }
    }

    private fun subscribeUi(){
        binding.fabStart.setOnClickListener {
            binding.glStartRunMain.setGuidelinePercent(0.6F)
            binding.ctlBeforeStart.visibility = View.GONE
            binding.ctlAfterStart.visibility = View.VISIBLE
            viewModel.onStartClick()
        }

        binding.fabPauseOrResume.setOnClickListener {
            viewModel.onPauseOrResumeClick()
        }

        binding.fabFinish.setOnClickListener {
//            Timber.d("Before: ${binding.mapView.height} ${binding.mapView.width}")
//            val mapWidth = binding.mapView.width
//            val mapHeight = (mapWidth * 0.8).toInt()
//
//            val layoutParams = binding.mapView.layoutParams
//            layoutParams.height = mapHeight
//
//            binding.mapView.layoutParams = layoutParams
//
//            Timber.d("After: ${binding.mapView.height} ${binding.mapView.width}")

            viewModel.onFinishClick(binding.mapView.height, binding.mapView.width)
        }

    }

    private fun startMainActivity(){
        Intent(this, MainActivity::class.java).also {
            this.startActivity(it)
            this.finish()
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