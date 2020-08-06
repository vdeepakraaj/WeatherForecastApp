package com.deepak.weatherapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deepak.weatherapplication.dao.ForeCastDao
import com.deepak.weatherapplication.data.model.Forecast

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Forecast::class], version = 8, exportSchema = false)
abstract class ForeCastRoomDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForeCastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ForeCastRoomDatabase? = null

        fun getDatabase(context: Context): ForeCastRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForeCastRoomDatabase::class.java,
                    "ForeCastRoomDatabase"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}