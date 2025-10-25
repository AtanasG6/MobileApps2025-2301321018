package com.example.weathertravelplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val city: String,
    val startDate: Long,
    val endDate: Long,
    val notes: String = ""
)