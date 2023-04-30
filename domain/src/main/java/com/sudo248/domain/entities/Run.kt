package com.sudo248.domain.entities
import java.util.Date

data class Run(
    val runId: String,
    val name: String,
    val distance: Float, // meter
    val avgSpeed: Float,
    val timeRunning: Int,
    // co the khong luu duoc anh
    val imageInByteArray: ByteArray?,
    val caloBurned: Int,
    val timeStart: Date,
    val stepCount: Int,
    val location: String,
    val imageUrl: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Run

        if (runId != other.runId) return false
        if (name != other.name) return false
        if (distance != other.distance) return false
        if (avgSpeed != other.avgSpeed) return false
        if (timeRunning != other.timeRunning) return false
        if (!imageInByteArray.contentEquals(other.imageInByteArray)) return false
        if (caloBurned != other.caloBurned) return false
        if (timeStart != other.timeStart) return false
        if (location != other.location) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = runId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + distance.hashCode()
        result = 31 * result + avgSpeed.hashCode()
        result = 31 * result + timeRunning
        result = 31 * result + imageInByteArray.contentHashCode()
        result = 31 * result + caloBurned
        result = 31 * result + timeStart.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}
