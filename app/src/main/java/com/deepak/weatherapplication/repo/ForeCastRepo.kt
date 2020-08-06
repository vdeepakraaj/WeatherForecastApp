package com.deepak.weatherapplication.repo

import com.deepak.weatherapplication.dao.ForeCastDao
import com.deepak.weatherapplication.data.model.Forecast

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
//class ForeCastRepo(private val foreCastDao: ForeCastDao) {
//
//  // Room executes all queries on a separate thread.
//  // Observed LiveData will notify the observer when the data has changed.
//  val allForecast: LiveData<List<Forecast>> = foreCastDao.getForecast()
//
//  fun insert(foreCast: Forecast) {
//    foreCastDao.insert(foreCast)
//  }
//}
class ForeCastRepo(private val forcastDao: ForeCastDao) {
    // Room executes all queries on a separate thread.

    suspend fun insert(list: List<Forecast>) {
        forcastDao.deleteAndInsert(list)
    }

    suspend fun fetch(): List<Forecast> {
        return forcastDao.getForecast()
    }
}