package com.sudo.jogingu.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.sudo.jogingu.R
import com.sudo.jogingu.common.Constant.REQUEST_CODE_LOCATION_PERMISSION
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

object TrackingPermission {
    fun hasLocationPermissions(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun requestPermission(activity: Activity){
        Timber.d("Request Permission for location")
        if(hasLocationPermissions(activity)){
            return
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                activity,
                activity.getString(R.string.request_permission),
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            EasyPermissions.requestPermissions(
                activity,
                activity.getString(R.string.request_permission),
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    }

    fun onPermissionsDenied(activity: Activity, perms: MutableList<String>){
        if(EasyPermissions.somePermissionPermanentlyDenied(activity, perms)){
            AppSettingsDialog.Builder(activity).build().show()
        }else{
            requestPermission(activity)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}