package com.sudo.data.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "targets")
data class TargetDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "target_id")
    val targetId: String,
    val distance: Int,
    val calo: Int,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean
)
