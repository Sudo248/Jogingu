package com.sudo.jogingu.common

import android.graphics.Color

object Constant {
    const val  REQUEST_CODE_LOCATION_PERMISSION = 0
    const val ACTION_START = "ACTION_START"
    const val ACTION_RUNNING = "ACTION_RUNNING"
    const val ACTION_PAUSE = "ACTION_PAUSE"
    const val ACTION_FINISH = "ACTION_FINISH"
    const val ACTION_SHOW_RUNNING_ACTIVITY = "ACTION_SHOW_RUNNING_ACTIVITY"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.GREEN
    const val POLYLINE_WIDTH_DEFAULT = 8f
    const val MAP_ZOOM_DEFAULT = 19f

    // toa do HN
    const val LATITUDE_DEFAULT = 21.028333
    const val LONGITUDE_DEFAULT = 105.853333

    const val NOTIFICATION_CHANNEL_ID  = "tracking-channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val MINUTE_DURATION = 60
    const val HOUR_DURATION = 3600

}