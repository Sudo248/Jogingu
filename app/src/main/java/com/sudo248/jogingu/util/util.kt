package com.sudo248.jogingu.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant.HOUR_DURATION
import com.sudo248.jogingu.common.Constant.MINUTE_DURATION
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun Int.toTimeHour(): Int {
    return this / HOUR_DURATION
}

fun Int.toTimeMinute(): Int {
    return this / MINUTE_DURATION
}

fun ImageView.loadImageFromUrl(url: String?, isShowLoading: Boolean = true) {

    Glide.with(this)
        .load(url)
        .placeholder(
            if (isShowLoading) {
                val circularProgressDrawable = CircularProgressDrawable(this.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()
                circularProgressDrawable
            } else {
                null
            }
        )
        .error(R.drawable.ic_error)
        .into(this)
}



