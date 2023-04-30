package com.sudo248.jogingu.util

import android.util.Size
import com.google.android.gms.maps.model.LatLng
import com.sudo248.domain.ktx.toStringAsFixed
import com.sudo248.jogingu.common.Constant

object UrlUtils {
    fun genStaticMapboxUrl(
        min: LatLng,
        max: LatLng,
        size: Size,
        encodePolyline: String,
        padding: Int = 20
    ): String {
        return "https://api.mapbox.com/styles/v1/mapbox/light-v11/static/path-3+37ff00($encodePolyline)/[${
            min.longitude.toStringAsFixed(4)
        },${min.latitude.toStringAsFixed(4)},${max.longitude.toStringAsFixed(4)},${
            max.latitude.toStringAsFixed(4)
        }]/${size.width}x${size.height}?padding=$padding,$padding,$padding,$padding&access_token=${Constant.MAPBOX_ACCESS_TOKEN}"
    }

    fun getBestFitStaticMapboxUrl(url: String, size: Size): String {
        val first = url.substringBefore("]/")
        val second = url.substringAfter("?padding=")
        return first + "]/${size.width}x${size.height}?padding=" + second
    }
}