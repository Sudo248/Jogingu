package com.sudo.data.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "runs")
data class RunDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "run_id")
    val runId: String,
    val name: String,
    val distance: Float,
    val avgSpeed: Float,
    @ColumnInfo(name = "time_running")
    val timeRunning: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "calo_burned")
    val caloBurned: Int,
    @ColumnInfo(name = "time_start")
    val timeStart: Date,
    val location: String
)
