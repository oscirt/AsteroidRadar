package com.example.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidsDAO {
    @Query("SELECT * FROM asteroids WHERE date >= :currentDate")
    fun getAsteroids(currentDate: String) : Flow<List<Asteroids>>

    @Query("SELECT date FROM asteroids ORDER BY date ASC LIMIT 1")
    suspend fun getLatestDate() : String?

    @Query("DELETE FROM asteroids")
    suspend fun deleteAllAsteroids()

    @Insert
    suspend fun addAsteroid(asteroids: List<Asteroids>)
}