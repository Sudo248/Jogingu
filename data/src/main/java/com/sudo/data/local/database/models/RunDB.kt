package com.sudo.data.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sudo.data.util.genId
import java.util.Date

@Entity(tableName = "runs")
data class RunDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "run_id")
    val runId: String = genId("Run"),
    val name: String = "Today",
    val distance: Float =  0.0f,
    val avgSpeed: Float = 0.0f,
    @ColumnInfo(name = "time_running")
    val timeRunning: Int = 0,
    @ColumnInfo(name = "image_url")
    val imageInByteArray: ByteArray? = null,
    @ColumnInfo(name = "calo_burned")
    val caloBurned: Int = 0,
    @ColumnInfo(name = "time_start")
    val timeStart: Date = Date(System.currentTimeMillis()),
    val location: String = "Viet Nam"
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RunDB

        if (runId != other.runId) return false
        if (name != other.name) return false
        if (distance != other.distance) return false
        if (avgSpeed != other.avgSpeed) return false
        if (timeRunning != other.timeRunning) return false
        if (!imageInByteArray.contentEquals(other.imageInByteArray)) return false
        if (caloBurned != other.caloBurned) return false
        if (timeStart != other.timeStart) return false
        if (location != other.location) return false

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
        return result
    }
}
