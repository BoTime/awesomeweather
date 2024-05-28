package com.botime.awesomeweather.data

import com.google.gson.annotations.SerializedName

data class FiveDayForecastResponse(
    @SerializedName("cod")
    val cod: String,
    val message: Float = 0.0F,
    @SerializedName("cnt")
    val count: Int,
    val list: List<ThreeHourForecast>,
    val city: City? = null,
)

val DEFAULT_WEATHER_DATA =
    FiveDayForecastResponse(cod = StatusCode.FAIL.value, count = 0, list = listOf())

enum class StatusCode(val value: String) {
    SUCCESS("200"),
    FAIL("404")
}

data class ThreeHourForecast(
    @SerializedName("dt")
    val utcTimestamp: Long,
    val main: Main3H,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Float,
    val pop: Float,
    val rain: Rain3H,
    val sys: Sys,
    @SerializedName("dt_txt")
    val datetimeText: String,
)

data class Coordinates(
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("lat")
    val latitude: Double,
)

data class Weather(val id: Int, val main: String, val description: String, val icon: String)

data class Main3H(
    @SerializedName("temp")
    val temperature: Float,
    @SerializedName("feels_like")
    val temperatureFeelsLike: Float,
    @SerializedName("temp_min")
    val temperatureMin: Float,
    @SerializedName("temp_max")
    val temperatureMax: Float,
    val pressure: Int,
    @SerializedName("sea_level")
    val seaLevel: Int,
    @SerializedName("grnd_level")
    val groundLevel: Int,
    val humidity: Int,
    @SerializedName("temp_kf")
    val tempKf: Float,
)

data class Wind(
    val speed: Float,
    @SerializedName("deg")
    val degree: Float,
    val gust: Float,
)

data class Rain3H(
    @SerializedName("3h")
    val threeHour: Float,
)

data class Sys(
    val pod: String,
)

data class Clouds(
    val all: Float,
)

data class City(
    val id: Int,
    val name: String,
    @SerializedName("coord")
    val coordinates: Coordinates,
    val country: String,
    val population: Int,
    val timezone: Int,
    @SerializedName("sunrise")
    val sunriseTimestamp: Long,
    @SerializedName("sunset")
    val sunsetTimestamp: Long,
)