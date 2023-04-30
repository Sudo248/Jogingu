package com.sudo248.data.local.database.models.firebase

import com.sudo248.domain.entities.Gender
import java.util.*
import kotlin.collections.HashMap

data class UserDocument(
    val userId: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val country: String = "",
    val primarySport: String = "",
    val gender: Gender = Gender.MALE,
    val birthday: Date = Date(System.currentTimeMillis()),
    val height: Int = 0,
    val weight: Int = 0,
    val imageUrl: String? = null
)
