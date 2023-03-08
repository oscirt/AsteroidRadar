package com.example.asteroidradar.models

data class Asteroid (
    val id: String,
    val name: String,
    val magnitude: Double,
    val isHazardous: Boolean,
    val estimatedDiameterMax: Double,
    var date: String = "",
    val close_date: String = "",
    val kps: Double,
    val ams: Double
)
