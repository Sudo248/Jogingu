package com.sudo.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "targets")
data class TargetDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "target_id")
    val targetId: Int,
    val distance: Int,
    val calo: Int,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean
)
