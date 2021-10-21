package com.sudo.domain.entities

import java.util.Date

data class User(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val city: String,
    val country: String,
    val primarySport: String,
    val gender: Gender,
    val birthday: Date,
    val age: Byte,
    val height: Short,
    val wight: Short,
    val imageUrl: String
)