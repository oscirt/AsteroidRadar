package com.example.asteroidradar.models

import com.squareup.moshi.Json

data class PictureOfTheDay (
    @field:Json(name = "url") val url: String,
    @field:Json(name = "media_type") val media_type: String,
    @field:Json(name = "title") val title: String
)