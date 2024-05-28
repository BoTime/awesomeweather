package com.botime.awesomeweather.data

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationTest {
    @Test
    fun `Should return a null location if input is empty`() {
        // Given
        val input = ""

        // When
        val location = convertToLocation(input)

        // Verify
        assertEquals(null, location)
    }

    @Test
    fun `Should return a valid location if input has only a city name`() {
        // Given
        val input = "Newark"

        // When
        val location = convertToLocation(input)

        // Verify
        assertEquals("Newark", location?.city)
    }

    @Test
    fun `Should return a valid location if input has a city name and a state name`() {
        // Given
        val input = "Newark, CA"

        // When
        val location = convertToLocation(input)

        // Verify
        assertEquals("Newark", location?.city)
        assertEquals("CA", location?.state)
    }

    @Test
    fun `Should return null if input has a city name, a state name and a country name`() {
        // Given
        val input = "Newark, CA, US"

        // When
        val location = convertToLocation(input)

        // Verify
        assertEquals(null, location)
    }
}