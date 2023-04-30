package com.sudo248.jogingu.ui.activities.run

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant
import com.sudo248.jogingu.common.RunState
import com.sudo248.jogingu.databinding.ActivityRunBinding
import com.sudo248.jogingu.databinding.DialogConfirmSaveRunBinding
import com.sudo248.jogingu.ui.activities.main.MainActivity
import com.sudo248.jogingu.ui.activities.run.viewmodel.GoogleMapViewModel
import com.sudo248.jogingu.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

@AndroidEntryPoint
class RunActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityRunBinding
    private val viewModel: GoogleMapViewModel by viewModels()
    private var isShowRunInfo = true

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.runState.collect {
                    when (it) {
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
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.runningTime.collect {
                    binding.tvTimeValue.text = TimeUtil.parseTime(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stepCounter.collect {
                    binding.tvStepValue.text = it.toString()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.distance.collect {
                    binding.tvDistance.text = "%.2f".format(it / 1000)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.avgSpeed.collect {
                    Timber.d("avgSpeed $it")
                    binding.tvSpeedValue.text = "%.2f".format(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSuccessToSaveRun.collect {
                    if (it) {
                        startMainActivity()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMapAsync() {
        if (TrackingPermission.hasLocationPermissions(this)) {
            binding.mapView.getMapAsync {
                it.isMyLocationEnabled = true
                it.moveCamera(CameraUpdateFactory.zoomTo(Constant.MAP_ZOOM_DEFAULT))
                viewModel.setMap(it)
            }
        }
    }

    private fun subscribeUi() {
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
            showConfirmSaveRunDialog()
        }

        binding.ctlAfterStart.setOnClickListener {
            if (isShowRunInfo) {
                hideRunInfo()
            } else {
                showRunInfo()
            }
            isShowRunInfo = !isShowRunInfo
        }
    }

    private fun showRunInfo() {
        binding.glStartRunMain.setGuidelinePercent(0.6F)
        binding.groupRunInfo.visible()
    }

    private fun hideRunInfo() {
        binding.glStartRunMain.setGuidelinePercent(0.8F)
        binding.groupRunInfo.gone()
    }

    private fun startMainActivity() {
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
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
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

    private fun showConfirmSaveRunDialog(): Dialog {
        viewModel.onFinishClick(binding.mapView.width)
        val dialog = Dialog(this)
        val dialogBinding = DialogConfirmSaveRunBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imageUrl.collect {
                    dialogBinding.imgDialog.loadImageFromUrl(it)
                    dialogBinding.txtSave.isEnabled = true
                }
            }
        }

        dialogBinding.apply {
            txtSave.setOnClickListener {
                viewModel.saveRunToDB()
            }

            txtContinue.setOnClickListener {
                viewModel.continueRun()
            }

            txtCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.showWithTransparentBackground()
        return dialog
    }

}