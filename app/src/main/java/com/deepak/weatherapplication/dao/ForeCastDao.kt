package com.deepak.weatherapplication.dao

import androidx.room.*
import com.deepak.weatherapplication.data.model.Forecast

@Dao
interface ForeCastDao {

    @Transaction
    @Query("SELECT * FROM weatherforecast ORDER BY id ASC")
    suspend fun getForecast(): List<Forecast>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Forecast>)

    @Query("DELETE FROM weatherforecast")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsert(forecast: List<Forecast>) {
        deleteAll()
        insertAll(forecast)
    }
}