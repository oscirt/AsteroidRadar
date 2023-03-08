package com.example.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Asteroids::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract fun asteroidDao() : AsteroidsDAO

    companion object {
        @Volatile
        private var INSTANCE: AsteroidsDatabase? = null

        fun getDatabase(context: Context): AsteroidsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AsteroidsDatabase::class.java,
                    "asteroids_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}