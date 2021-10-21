package com.sudo.data.util

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import com.sudo.domain.entities.Gender
import java.text.SimpleDateFormat
import java.util.Date

object Converter {
    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { format.parse(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let { format.format(it) }
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