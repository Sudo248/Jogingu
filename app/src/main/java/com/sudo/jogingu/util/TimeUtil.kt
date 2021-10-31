package com.sudo.jogingu.util

import com.sudo.jogingu.common.Constant.HOUR_DURATION
import com.sudo.jogingu.common.Constant.MINUTE_DURATION

object TimeUtil {
    fun parseTime(time: Long): String{
        var timeInSeconds = time
        val hours = timeInSeconds / HOUR_DURATION
        timeInSeconds %= HOUR_DURATION
        val minutes = timeInSeconds / MINUTE_DURATION
        timeInSeconds %= MINUTE_DURATION
        val seconds = timeInSeconds

        return if(hours > 0){
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        }else{
            "%02d : %02d".format(minutes, seconds)
        }
    }
}