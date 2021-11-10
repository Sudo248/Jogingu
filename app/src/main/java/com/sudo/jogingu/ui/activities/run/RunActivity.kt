package com.sudo.jogingu.ui.activities.run

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sudo.jogingu.R
import com.sudo.jogingu.common.RunState
import com.sudo.jogingu.databinding.ActivityRunBinding
import com.sudo.jogingu.ui.activities.run.viewmodel.GoogleMapViewModel
import com.sudo.jogingu.util.TimeUtil
import com.sudo.jogingu.util.TrackingPermission
import kotlinx.coroutines.flow.collect
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

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
        viewModel.context = this

        getMapAsync()
        subscribeUi()
        collect()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
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

    }

    @SuppressLint("MissingPermission")
    private fun getMapAsync(){
        if(TrackingPermission.hasLocationPermissions(this)){
            binding.mapView.getMapAsync {
                it.isMyLocationEnabled = true
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
            viewModel.onFinishClick()
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