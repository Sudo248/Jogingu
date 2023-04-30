package com.sudo248.domain.ktx

fun Double.toStringAsFixed(number: Int): String {
    return String.format("%.${number}f", this)
}