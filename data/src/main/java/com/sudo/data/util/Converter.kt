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

    @TypeConverter
    fun genderFromInt(value: Int = 0): Gender {
        return when(value){
            0 -> Gender.MALE
            1 -> Gender.FEMALE
            else -> Gender.OTHER
        }
    }

    @TypeConverter
    fun genderToInt(gender: Gender): Int {
        return when(gender){
            Gender.MALE -> 0
            Gender.FEMALE -> 1
            else -> 2
        }
    }

}