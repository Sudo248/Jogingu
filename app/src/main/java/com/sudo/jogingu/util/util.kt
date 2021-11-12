package com.sudo.jogingu.util

import android.graphics.Bitmap
import com.sudo.jogingu.common.Constant.HOUR_DURATION
import com.sudo.jogingu.common.Constant.MINUTE_DURATION
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray{
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun Int.toTimeHour(): Int{
    return this / HOUR_DURATION
}

fun Int.toTimeMinute(): Int{
    return this / MINUTE_DURATION
}



