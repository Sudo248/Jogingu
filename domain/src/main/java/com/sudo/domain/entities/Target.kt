package com.sudo.domain.entities

data class Target(
    val targetId: String,
    val distance: Int,
    val calo: Int,
    val isDone: Boolean
)
