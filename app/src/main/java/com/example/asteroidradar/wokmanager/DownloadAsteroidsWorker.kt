package com.example.asteroidradar.wokmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.database.Asteroids
import com.example.asteroidradar.database.AsteroidsDatabase
import com.example.asteroidradar.network.AsteroidsAPI
import com.example.asteroidradar.network.fromJson
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DownloadAsteroidsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            println("WORKER")
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date())
            val afterSevenDaysDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(calendar.time)
            val dao = AsteroidsDatabase.getDatabase(applicationContext).asteroidDao()
            val asteroids = fromJson(
                JSONObject(
                    AsteroidsAPI.service.getAsteroidsList(
                        currentDate,
                        afterSevenDaysDate
                    )
                )
            )
            dao.deleteAllAsteroids()
            dao.addAsteroid(asteroids.map {
                Asteroids(
                    it.id,
                    it.name,
                    it.magnitude,
                    it.isHazardous,
                    it.estimatedDiameterMax,
                    it.date,
                    it.close_date,
                    it.kps,
                    it.ams
                )
            })

            Result.success()
        } catch (e: Exception) {
            Log.d("TAG", "doWork: ${e.message}")
            Result.retry()
        }
    }
}