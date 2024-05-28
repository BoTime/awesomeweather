package com.botime.awesomeweather.repository

import com.botime.awesomeweather.api.WeatherService
import com.botime.awesomeweather.data.Coordinates
import com.botime.awesomeweather.data.DEFAULT_WEATHER_DATA
import com.botime.awesomeweather.data.FiveDayForecastResponse
import com.botime.awesomeweather.data.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrieves weather data.
 */
@Singleton
class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService,
) {
    fun getWeatherDataByCityName(location: Location): Flow<FiveDayForecastResponse> =
        flow {
            val queryMap = mapOf(
                QUERY_KEY to "${location},$COUNTRY_CODE",
                APP_ID_KEY to API_KEY
            )
            val response = weatherService.getFiveDayThreeHourWeatherForecast(queryMap).execute()
            emit(response.body() ?: DEFAULT_WEATHER_DATA)
        }

    fun getWeatherDataByCoordinates(coordinates: Coordinates): Flow<FiveDayForecastResponse> =
        flow {
            val queryMap = mapOf(
                LONGITUDE_KEY to "${coordinates.longitude}",
                LATITUDE_KEY to "${coordinates.latitude}",
                APP_ID_KEY to API_KEY,
            )
            val response = weatherService.getFiveDayThreeHourWeatherForecast(queryMap).execute()
            emit(response.body() ?: DEFAULT_WEATHER_DATA)
        }

    companion object {
        // TODO: Move out of the source code. For example to local.properties.
        private const val API_KEY = "32771d3af2da48f98f39bceb0855b698"
        private const val APP_ID_KEY = "appid"
        private const val QUERY_KEY = "q"
        private const val LONGITUDE_KEY = "lon"
        private const val LATITUDE_KEY = "lat"
        private const val COUNTRY_CODE = "us"
    }
}