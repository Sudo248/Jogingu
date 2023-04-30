package com.sudo248.jogingu.util

import android.content.Context
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant.HOUR_DURATION
import com.sudo248.jogingu.common.Constant.MINUTE_DURATION
import java.util.*

object TimeUtil {
    fun parseTime(time: Int, withLetter: Boolean = false): String{
        var timeInSeconds = time
        val hours = timeInSeconds.toTimeHour()
        timeInSeconds %= HOUR_DURATION
        val minutes = timeInSeconds.toTimeMinute()
        timeInSeconds %= MINUTE_DURATION
        val seconds = timeInSeconds

        return if(hours > 0){
            if (withLetter){
                "%dh %02dm".format(hours, minutes, seconds)
            }
            else{
                "%02d:%02d:%02d".format(hours, minutes, seconds)
            }
        }else{
            if(withLetter){
                "%dm  %02ds".format(minutes, seconds)
            }
            else{
                "%02d : %02d".format(minutes, seconds)
            }
        }
    }

    fun getNameRunByTime(context: Context, time: Long): String{
        val calender = Calendar.getInstance()
        calender.timeInMillis = time
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        return context.getString(
            when {
                hour > 12 -> {
                    R.string.morning_run
                }
                hour > 18 -> {
                    R.string.afternoon_run
                }
                else -> {
                    R.string.evening_run
                }
            }
        )
    }

}