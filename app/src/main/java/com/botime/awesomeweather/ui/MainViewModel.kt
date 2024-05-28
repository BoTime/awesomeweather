package com.botime.awesomeweather.ui

import androidx.lifecycle.*
import com.botime.awesomeweather.data.*
import com.botime.awesomeweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var _weatherData = MutableStateFlow(DEFAULT_WEATHER_DATA)
    val weatherData = _weatherData.asStateFlow()

    private var _cityNameSearched = MutableStateFlow(DEFAULT_CITY_NAME)
    val cityNameSearched = _cityNameSearched.asStateFlow()

    private var _showErrorMessage = MutableStateFlow(false)
    val showErrorMessage = _showErrorMessage.asStateFlow()

    /**
     * Get 5-day weather data by the name of a city with a 3-hour step.
     */
    fun refreshWeatherData(location: Location) {
        Timber.d("Refresh weather data by the name of a city")
        viewModelScope.launch {
            weatherRepository.getWeatherDataByCityName(location)
                .flowOn(ioDispatcher)
                .collect {
                    handleResponse(it)
                }
        }
    }

    /**
     * Get 5-day weather data by the longitude and latitude of a city with a 3-hour step.
     */
    fun refreshWeatherData(coordinates: Coordinates) {
        Timber.d("Refresh weather data by the coordinates of a city")
        viewModelScope.launch {
            weatherRepository.getWeatherDataByCoordinates(coordinates)
                .flowOn(ioDispatcher)
                .collect {
                    handleResponse(it)
                }
        }
    }

    private fun handleResponse(fiveDayForecastResponse: FiveDayForecastResponse) {
        when (fiveDayForecastResponse.cod) {
            StatusCode.SUCCESS.value -> {
                _showErrorMessage.value = false
                _weatherData.value = fiveDayForecastResponse
                fiveDayForecastResponse.city?.let { city ->
                    _cityNameSearched.value = "${city.name},${city.country}"
                }
            }
            else -> {
                _showErrorMessage.value = true
            }
        }
    }

    companion object {
        private const val DEFAULT_CITY_NAME = ""
    }
}