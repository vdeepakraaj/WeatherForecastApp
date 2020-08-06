package com.deepak.weatherapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deepak.weatherapplication.data.model.Forecast
import com.deepak.weatherapplication.injection.qualifiers.LastUpdateTime
import com.deepak.weatherapplication.repo.ForeCastRepo
import com.deepak.weatherapplication.repo.WeatherRepo
import com.deepak.weatherapplication.ui.base.BaseViewModel
import com.deepak.weatherapplication.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(@LastUpdateTime private val time: Long) : BaseViewModel() {

    @Inject
    lateinit var weatherRepo: WeatherRepo

    private var _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var _forecast: MutableLiveData<List<Forecast>> = MutableLiveData(listOf())
    private var forecastList = mutableListOf<Forecast>()
    private var _isInserted: MutableLiveData<Boolean> = MutableLiveData()

    //Encapsulating the mutable live data variable
    val dataLoading: LiveData<Boolean> = _dataLoading
    val isInserted: LiveData<Boolean> = _isInserted
    val forecast: LiveData<List<Forecast>> = _forecast

    private var repository: ForeCastRepo? = null
    private var latitude: String? = null
    private var longitude: String? = null


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadDataCoroutine() {
        _dataLoading.postValue(true)
        repository?.fetch()?.let {
            if (it.isNotEmpty() && checkIfLessThanFiveHours(it[0].dt)) {
                _forecast.postValue(repository?.fetch())
            } else {
                fetchFromAPI()
            }
        }
    }

    private fun checkIfLessThanFiveHours(it: Long) =
        System.currentTimeMillis().minus(it) < time

    private fun fetchFromAPI() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherListResult = weatherRepo.getWeatherForecast(latitude, longitude)
            _dataLoading.postValue(false)
            when (weatherListResult) {
                is NetworkResult.Success -> {
                    weatherListResult.body.list.mapIndexed { index, value ->
                        forecastList.add(
                            Forecast(
                                index,
                                value.getDay()
                                    ?: "",
                                value.main.getTempMinString(),
                                value.main.getTempMaxString(),
                                value
                                    .weather[0].icon,
                                value.weather[0].description,
                                System.currentTimeMillis()
                            )
                        )
                    }
                    val list = forecastList.distinctBy { it.day }
                    repository?.insert(list)
                    _isInserted.postValue(true)
                }
                is NetworkResult.Failure -> {
                    Timber.e("onError")
                }
            }
        }
    }

    suspend fun retrieve() {
        _forecast.postValue(repository?.fetch())
    }

    fun init(foreCastRepo: ForeCastRepo, latitude: String, longitude: String) {
        this.repository = foreCastRepo
        this.latitude = latitude
        this.longitude = longitude
    }
}