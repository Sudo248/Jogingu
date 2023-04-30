package com.sudo248.domain.entities

data class UserRun(
    val userId: String?,
    val firstName: String = "",
    val lastName: String = "",
    val userImageUrl: String? = null,
    val run: Run
)
