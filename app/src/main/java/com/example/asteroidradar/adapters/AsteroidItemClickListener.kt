package com.example.asteroidradar.adapters

import com.example.asteroidradar.models.Asteroid

class AsteroidItemClickListener(private val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}