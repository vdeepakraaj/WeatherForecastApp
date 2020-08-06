package com.deepak.weatherapplication.data.model

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherforecast")
data class Forecast(
    @PrimaryKey
    val id: Int,
    val day: String,
    val tempMin: String,
    val tempMax: String, val
    weatherIcon: String, val desc: String,
    val dt: Long
)

@RequiresApi(Build.VERSION_CODES.O)
fun getColor(dayOfWeek: String): Int {
    return when (dayOfWeek.toUpperCase()) {
        "MONDAY" -> Color.parseColor("#28E0AE")
        "TUESDAY" -> Color.parseColor("#FF0090")
        "WEDNESDAY" -> Color.parseColor("#FFAE00")
        "THURSDAY" -> Color.parseColor("#0090FF")
        "FRIDAY" -> Color.parseColor("#DC0000")
        "SATURDAY" -> Color.parseColor("#0051FF")
        "SUNDAY" -> Color.parseColor("#3D28E0")
        else -> Color.parseColor("#28E0AE")
    }
}