package com.sudo.data.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sudo.domain.entities.Gender
import java.util.Date

@Entity(tableName = "users")
data class UserDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    val city: String,
    val country: String,
    @ColumnInfo(name = "primary_sport")
    val primarySport: String,
    val gender: Gender,
    @ColumnInfo(name = "date_of_birth")
    val birthday: Date,
    val height: Short,
    val wight: Short,
    val imageUrl: String
)
