package com.deepak.weatherapplication.data.model

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Parcelize
data class WeatherData(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<WeatherItem>,
    @SerializedName("dt_txt") val dtTxt: String

) : Parcelable {

    fun getDay(): String? {
        return dt.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getDateTime(it)?.getDisplayName(TextStyle.FULL, Locale.getDefault())
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: Long): DayOfWeek? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(s * 1000)
            val formattedDate = sdf.format(netDate)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.of(
                    formattedDate.substringAfterLast("/").toInt(),
                    formattedDate.substringAfter("/").take(2).toInt(),
                    formattedDate.substringBefore("/").toInt()
                )
                    .dayOfWeek
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DayOfWeek.MONDAY
        }
    }


    @Parcelize
    data class Main(
        @SerializedName("temp_min") val tempMin: Double,
        @SerializedName("temp_max") val tempMax: Double
    ) : Parcelable {

        fun getTempMinString(): String {
            return tempMin.minus(273.15f).toString().substringBefore(".") + "°"
        }

        fun getTempMaxString(): String {
            return tempMax.minus(273.15f).toString().substringBefore(".") + "°"
        }
    }

}

