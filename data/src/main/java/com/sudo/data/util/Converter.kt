package com.sudo.data.util

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import com.sudo.domain.entities.Gender
import java.text.SimpleDateFormat
import java.util.Date

internal object Converter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}