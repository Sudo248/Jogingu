package com.sudo248.data.shared_preference

import com.sudo248.domain.entities.Gender
import java.util.*

object PrefKeys{
    const val namePrefs = "JoginguPrefs"
    const val isFirstOpenApp = "is_first_open_app"
    const val isSetupUserInfo = "isSetupUserInfo"
}

object TargetKeys{
    const val distance = "distance_target"
    const val calo= "calo_target"
    const val timeStart= "time_start_target"
    const val place= "place_target"
    const val recursive = "recursive_target"
    const val notificationBefore= "notification_before_target"
}

object UserKeys{
    const val userId = "user_id"
    const val firstName = "first_name"
    const val lastName = "last_name"
    const val city = "city"
    const val country = "country"
    const val primarySport = "primary_sport"
    const val gender = "gender"
    const val birthday = "birthday"
    const val height = "height"
    const val weight = "weight"
    const val imageInByteArray = "image_byte_array"
    const val imageUrl = "image_url"
}