package com.sudo248.domain.entities

import java.util.Date

data class User(
    val userId: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val country: String = "",
    val primarySport: String = "",
    val gender: Gender = Gender.MALE,
    val birthday: Date = Date(System.currentTimeMillis()),
    val age: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val imageInByteArray: ByteArray? = null,
    val imageUrl: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if(userId != other.userId) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (city != other.city) return false
        if (country != other.country) return false
        if (primarySport != other.primarySport) return false
        if (gender != other.gender) return false
        if (birthday != other.birthday) return false
        if (age != other.age) return false
        if (height != other.height) return false
        if (weight != other.weight) return false
        if (imageInByteArray != null) {
            if (other.imageInByteArray == null) return false
            if (!imageInByteArray.contentEquals(other.imageInByteArray)) return false
        } else if (other.imageInByteArray != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + primarySport.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + birthday.hashCode()
        result = 31 * result + age
        result = 31 * result + height
        result = 31 * result + weight
        result = 31 * result + (imageInByteArray?.contentHashCode() ?: 0)
        return result
    }
}