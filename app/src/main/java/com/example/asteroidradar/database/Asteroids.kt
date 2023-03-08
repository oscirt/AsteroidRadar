package com.example.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids")
data class Asteroids (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "absolute_magnitude") val magnitude: Double,
    @ColumnInfo(name = "is_hazardous") val isHazardous: Boolean,
    @ColumnInfo(name = "estimated_diameter_max") val estimatedDiameter: Double,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "close_date") val close_date: String,
    @ColumnInfo(name = "kilometer_per_second") val kps: Double,
    @ColumnInfo(name = "astronomical_miss_distance") val ams: Double
)