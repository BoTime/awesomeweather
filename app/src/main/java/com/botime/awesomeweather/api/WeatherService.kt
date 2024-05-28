package com.botime.awesomeweather.api

import com.botime.awesomeweather.data.FiveDayForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherService {
    /**
     * Get whether information from openweathermap.org.
     * @param city name of the city
     * @param country name of the country. The default value is "US".
     */
    @GET("data/2.5/forecast")
    fun getFiveDayThreeHourWeatherForecast(@QueryMap queryMap: Map<String, String>): Call<FiveDayForecastResponse>
}