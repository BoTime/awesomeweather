package com.botime.awesomeweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.botime.awesomeweather.R
import com.botime.awesomeweather.data.Coordinates
import com.botime.awesomeweather.data.DEFAULT_WEATHER_DATA
import com.botime.awesomeweather.data.convertToLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    private var currentLocation: ImageView? = null

    private var cityNameInput: EditText? = null

    private var searchButton: Button? = null

    private var weatherInformation: RecyclerView? = null

    private var weatherDataAdapter: WeatherDataAdapter? = null

    private var errorMessage: TextView? = null

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.d("Location permission granted")
                val locationManager =
                    context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                    lifecycleScope.launch {
                        mainViewModel.refreshWeatherData(Coordinates(it.longitude, it.latitude))
                    }
                }

            } else {
                Timber.d("Location permission not granted")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentLocation = view.findViewById(R.id.currentLocation_imageView)
        cityNameInput = view.findViewById(R.id.cityNameInput_editText)
        weatherInformation = view.findViewById(R.id.weatherInformation_recyclerView)
        searchButton = view.findViewById(R.id.search_button)
        errorMessage = view.findViewById(R.id.errorMessage_textView)

        // Read the last searched city name
        val sharedPreferences: SharedPreferences =
            view.context.getSharedPreferences(LOCAL_SEARCH_HISTORY, MODE_PRIVATE)
        val cityName = sharedPreferences.getString(KEY_CITY, PLACEHOLDER)
        if (cityName?.isNotEmpty() == true) {
            convertToLocation(cityName)?.let { location ->
                mainViewModel.refreshWeatherData(location)
            }
        }
        cityNameInput?.setText(cityName, TextView.BufferType.EDITABLE)

        // Get weather information of the current location
        currentLocation?.setOnClickListener {
            if (!hasLocationPermission()) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

        // Configure the RecyclerView
        weatherInformation?.layoutManager = LinearLayoutManager(this.context)
        weatherDataAdapter = WeatherDataAdapter(DEFAULT_WEATHER_DATA)
        weatherInformation?.adapter = weatherDataAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.weatherData.collect {
                    weatherDataAdapter?.refresh(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.cityNameSearched.collect {
                    sharedPreferences.edit().putString(KEY_CITY, it).apply()
                    cityNameInput?.setText(it, TextView.BufferType.EDITABLE)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.showErrorMessage.collect { showErrorMessage ->
                    if (showErrorMessage) {
                        errorMessage?.visibility = View.VISIBLE
                        weatherInformation?.visibility = View.GONE
                    } else {
                        errorMessage?.visibility = View.GONE
                        weatherInformation?.visibility = View.VISIBLE
                    }
                }
            }
        }

        searchButton?.setOnClickListener {
            cityNameInput?.text?.toString()?.let {
                sharedPreferences.edit().putString(KEY_CITY, it).apply()
                convertToLocation(it)?.let { location ->
                    mainViewModel.refreshWeatherData(location)
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return false
    }

    companion object {
        fun newInstance() = MainFragment()
        private const val LOCAL_SEARCH_HISTORY = "LastSearchedCity"
        private const val KEY_CITY = "city"
        private const val PLACEHOLDER = ""
    }
}