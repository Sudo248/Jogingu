package com.sudo248.jogingu.common

import android.graphics.Color
import android.os.Environment
import java.io.File

object Constant {
    const val  REQUEST_CODE_LOCATION_PERMISSION = 0
    const val ACTION_START = "ACTION_START"
    const val ACTION_RUNNING = "ACTION_RUNNING"
    const val ACTION_PAUSE = "ACTION_PAUSE"
    const val ACTION_FINISH = "ACTION_FINISH"

    const val ACTION_SETUP_EVERYDAY_NOTIFICATION = "ACTION_SETUP_EVERYDAY_NOTIFICATION"
    const val ACTION_SETUP_RUN_NOTIFICATION = "ACTION_SETUP_RUN_NOTIFICATION"
    const val ACTION_NOTIFICATION_EVERYDAY = "ACTION_NOTIFICATION_EVERYDAY"
    const val ACTION_NOTIFICATION_RUN = "ACTION_NOTIFICATION_RUN"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.GREEN
    const val POLYLINE_WIDTH_DEFAULT = 8f
    const val MAP_ZOOM_DEFAULT = 19f

    // toa do HN
    const val LATITUDE_DEFAULT = 21.028333
    const val LONGITUDE_DEFAULT = 105.853333

    const val NOTIFICATION_RUNNING_CHANNEL_ID  = "tracking-channel"
    const val NOTIFICATION_RUNNING_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_RUNNING_ID = 1

    const val NOTIFICATION_EVERYDAY_CHANNEL_ID = "everyday-channel"
    const val NOTIFICATION_EVERYDAY_CHANNEL_NAME = "Everyday"
    const val NOTIFICATION_EVERYDAY_ID = 2

    const val NOTIFICATION_RUN_CHANNEL_ID = "Run-channel"
    const val NOTIFICATION_RUN_CHANNEL_NAME = "Run"
    const val NOTIFICATION_RUN_ID = 3

    const val KEY_TITLE_NOTIFICATION = "KEY_TITLE_NOTIFICATION"
    const val KEY_CONTENT_NOTIFICATION = "KEY_CONTENT_NOTIFICATION"
    const val KEY_USER_INFO = "KEY_USER_INFO"

    const val MINUTE_DURATION = 60
    const val HOUR_DURATION = 3600

    const val TIME_NOTIFICATION = "TIME_NOTIFICATION"
    const val OPEN_FRAGMENT = "OPEN_FRAGMENT"

    const val MAPBOX_ACCESS_TOKEN = "pk.eyJ1Ijoic3VkbzI0OCIsImEiOiJja3V2OWFjNjk2NnNhMnBxNm5vZXFjbnFyIn0.ehPEJpjmAIsW1cR2lGAPxg"

    const val MAPBOX_MAP_STYLE = "mapbox://styles/sudo248/cl0lth5mb000415qq5j6u009y"

}