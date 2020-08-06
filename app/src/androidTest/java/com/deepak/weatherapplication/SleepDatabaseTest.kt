package com.deepak.weatherapplication

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.deepak.weatherapplication.dao.ForeCastDao
import com.deepak.weatherapplication.data.model.Forecast
import com.deepak.weatherapplication.database.ForeCastRoomDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class ForecastDBTest {

    private lateinit var forecastDao: ForeCastDao
    private lateinit var db: ForeCastRoomDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, ForeCastRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        forecastDao = db.forecastDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGet() = runBlocking {
        val forecast = listOf(Forecast(0, "Monday", "20", "23", "03d", "Cloudy", 1000L))
        GlobalScope.launch { forecastDao.deleteAndInsert(forecast) }
        val allForecast = forecastDao.getForecast()
        assertEquals(allForecast[0].day, "Monday")
    }

}
