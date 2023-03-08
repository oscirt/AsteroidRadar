package com.example.asteroidradar.adapters

class DefaultClickListener(private val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}