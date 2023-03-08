package com.example.asteroidradar.network

import com.example.asteroidradar.models.Asteroid
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun fromJson(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearObjects = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = arrayListOf<Asteroid>()

    val nextSevenDates = getNextSevenDaysFormattedDates()
    for (date in nextSevenDates) {
        if (nearObjects.has(date)) {
            val dateAsteroidJsonArray = nearObjects.getJSONArray(date)
            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getString("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val closeDate = closeApproachData.getString("close_approach_date")
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(
                    id,
                    codename,
                    absoluteMagnitude,
                    isPotentiallyHazardous,
                    estimatedDiameter,
                    date,
                    closeDate,
                    relativeVelocity,
                    distanceFromEarth
                )
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..7) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}