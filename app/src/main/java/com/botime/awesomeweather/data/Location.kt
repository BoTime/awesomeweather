package com.botime.awesomeweather.data

data class Location(val city: String, val state: String, val country: String = "US") {
    override fun toString(): String {
        return if (state.isEmpty()) {
            city
        } else {
            "${city},${state},${country}"
        }
    }
}

/**
 * Convert the city name input from users into a [Location].
 * Currently we are only supporting U.S cities.
 */
fun convertToLocation(input: String): Location? {
    val segments = input.split(",")
    if (segments.isEmpty() || segments.size > 2) {
        return null
    }

    val city = segments[0].trim()
    if (city.isEmpty()) {
        return null
    }

    val state = if (segments.size == 2) {
        segments[1].trim()
    } else {
        ""
    }

    return Location(city, state)
}