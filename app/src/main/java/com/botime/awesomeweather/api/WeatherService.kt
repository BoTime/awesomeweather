package com.botime.awesomeweather.api

import com.botime.awesomeweather.data.FiveDayForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Get whether information from openweathermap.org.
 */
interface WeatherService {
    /**
     * Get 5 day weather forecast of a city with 3-hour step.
     *
     * @see [5 day weather forecast](]https://openweathermap.org/forecast5#builtin)
     * @param queryMap search parameters.
     */
    @GET("data/2.5/forecast")
    fun getFiveDayThreeHourWeatherForecast(@QueryMap queryMap: Map<String, String>): Call<FiveDayForecastResponse>
}