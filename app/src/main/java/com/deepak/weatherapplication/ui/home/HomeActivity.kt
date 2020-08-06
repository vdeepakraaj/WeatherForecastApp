package com.deepak.weatherapplication.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deepak.weatherapplication.data.model.Forecast
import com.deepak.weatherapplication.database.ForeCastRoomDatabase
import com.deepak.weatherapplication.repo.ForeCastRepo
import com.deepak.weatherapplication.ui.base.BaseActivity
import com.deepak.weatherapplication.ui.forecast.ForeCastAdapter
import com.deepak.weatherapplication.utils.ImageUtils
import com.deepak.weatherapplication.utils.PermissionUtils
import com.deepak.weatherforecast.R
import com.deepak.weatherforecast.databinding.ActivityHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    //creating an arraylist to store foreCastList using the data class Forecast
    private val foreCastList = arrayListOf<Forecast>()

    //creating our adapter
    val adapter = ForeCastAdapter()

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun layoutId(): Int = R.layout.activity_home

    var fusedLocationClient: FusedLocationProviderClient? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        llProgressBar.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize(latitude: String, longitude: String) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val wordsDao = ForeCastRoomDatabase.getDatabase(this).forecastDao()
        viewModel.init(ForeCastRepo(wordsDao), latitude, longitude)

        GlobalScope.launch { viewModel.loadDataCoroutine() }
        viewModel.isInserted.observe(this, Observer {
            GlobalScope.launch { viewModel.retrieve() }
        })

        viewModel.forecast.observe(this, Observer { list ->
            if (list.isNullOrEmpty().not()) {
                textViewDayOfWeek.text = list[0].day
                textViewTempMin.text = getString(R.string.min).plus(" ").plus(list[0].tempMin)
                textViewTempMax.text = getString(R.string.max).plus(" ").plus(list[0].tempMax)
                textViewDesc.text = list[0].desc
                ImageUtils.setWeatherIcon(imgWeather, list[0].weatherIcon)
                foreCastList.clear()
                list.map {
                    foreCastList.add(
                        Forecast(
                            0,
                            it.day,
                            it.tempMin,
                            it.tempMax,
                            it.weatherIcon,
                            it.desc,
                            it.dt
                        )
                    )
                }
                adapter.setItems(foreCastList.subList(1, foreCastList.size))
                llProgressBar.visibility = View.GONE
            }
        })

        //adding a layoutmanager
        binding.myRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        adapter.notifyDataSetChanged()

        //now adding the adapter to recyclerview
        binding.myRecyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 1 hr with high accuracy
        val locationRequest = LocationRequest().setInterval(60 * 10000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    var latitude = ""
                    var longitude = ""
                    for (location in locationResult.locations) {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }
                    initialize(latitude, longitude)
                }
            },
            null
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                    Timer("Exitting", false).schedule(1000) {
                        exitProcess(0)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // close the primary database to ensure all the transactions are merged
        ForeCastRoomDatabase.getDatabase(this).close()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }
}