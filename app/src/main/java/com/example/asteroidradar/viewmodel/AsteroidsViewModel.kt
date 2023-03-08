package com.example.asteroidradar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.asteroidradar.database.AsteroidsDAO
import com.example.asteroidradar.database.AsteroidsDatabase
import com.example.asteroidradar.database.Asteroids
import com.example.asteroidradar.models.Asteroid
import com.example.asteroidradar.models.PictureOfTheDay
import com.example.asteroidradar.network.AsteroidsAPI
import com.example.asteroidradar.network.fromJson
import com.example.asteroidradar.wokmanager.DownloadAsteroidsWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

enum class Status { LOADING, ERROR, DONE }

class AsteroidsViewModel(application: Application) : AndroidViewModel(application) {
    private var _currentAsteroid = MutableLiveData<Asteroid>()
    val currentAsteroid: LiveData<Asteroid> get() = _currentAsteroid

    private var _pictureOfTheDay = MutableLiveData<PictureOfTheDay>()
    val pictureOfTheDay: LiveData<PictureOfTheDay> get() = _pictureOfTheDay

    private val dao: AsteroidsDAO

    private val WORK_NAME = "DOWNLOAD_WORK"
    private val workManager = WorkManager.getInstance(application)
    private val request = PeriodicWorkRequestBuilder<DownloadAsteroidsWorker>(1, TimeUnit.DAYS)
        .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED, requiresCharging = true))
        .setInitialDelay(1, TimeUnit.DAYS)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MICROSECONDS
        )
        .build()


    private val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date())

    init {
        _pictureOfTheDay.value = PictureOfTheDay("", "ERROR", "")
        dao = AsteroidsDatabase.getDatabase(application).asteroidDao()
        loadPicture()

        connect()

        workManager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    private fun loadPicture() {
        viewModelScope.launch {
            try {
                val pictureOfTheDay = AsteroidsAPI.service.getPictureOfTheDay()
                _pictureOfTheDay.value = pictureOfTheDay
            } catch (e: Exception) {
                Log.d("TAG", "loadPicture: ${e.message}")
            }
        }
    }

    private suspend fun loadData(startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                val list = fromJson(
                    JSONObject(
                        AsteroidsAPI.service
                            .getAsteroidsList(startDate, endDate)
                    )
                )
                dao.deleteAllAsteroids()
                dao.addAsteroid(list.map {
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
            } catch ( _ : SocketTimeoutException) {
                loadData(startDate, endDate)
            } catch (e: Exception) {
                Log.d("TAG", "loadData: ${e.javaClass}")
            }
        }.join()
    }

    fun setCurrentAsteroid(asteroid: Asteroid) {
        _currentAsteroid.value = asteroid
    }

    fun allAsteroids(): Flow<List<Asteroids>> = dao.getAsteroids(currentDate)

    fun oneDayAsteroids(): Flow<List<Asteroids>> = dao.getOneDayAsteroids(currentDate)

    private fun connect() {
        viewModelScope.launch {
            val latestDate = dao.getLatestDate()
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val afterSevenDaysDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(calendar.time)
            if (latestDate != null) {
                if (latestDate != currentDate) {
                    loadData(currentDate, afterSevenDaysDate)
                }
            } else {
                loadData(currentDate, afterSevenDaysDate)
            }
        }
    }
}