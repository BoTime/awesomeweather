package com.botime.awesomeweather.ui

import com.botime.awesomeweather.data.*
import com.botime.awesomeweather.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val weatherRepository = mockk<WeatherRepository>()

    private val losAngeles = "Los Angeles"

    private val countryUs = "US"

    private val coordinates = Coordinates(34.0549, -118.2445)

    private val city = City(
        id = 5368361,
        name = losAngeles,
        coordinates = coordinates,
        country = countryUs,
        population = 3792621,
        timezone = 25200,
        sunriseTimestamp = 1716900240,
        sunsetTimestamp = 1716951422
    )

    private val response200 =
        FiveDayForecastResponse(
            cod = StatusCode.SUCCESS.value,
            count = 1,
            list = listOf(),
            city = city
        )

    private val response404 =
        FiveDayForecastResponse(cod = StatusCode.FAIL.value, count = 0, list = listOf())

    private val invalidLocation = Location("asdf", "CA")

    private val validLocation = Location(losAngeles, "CA")

    @Before
    fun setup() {

    }

    @Test
    fun `Should receive weather data successfully when location is valid and status code 200 is received`() =
        runTest {
            // Given
            every {
                weatherRepository.getWeatherDataByCityName(validLocation)
            } returns flow {
                emit(response200)
            }
            val dispatcher = StandardTestDispatcher(this.testScheduler)
            Dispatchers.setMain(dispatcher)

            // When
            val mainViewModel = MainViewModel(weatherRepository, dispatcher)
            mainViewModel.refreshWeatherData(validLocation)
            advanceUntilIdle()

            // Verify
            verify {
                weatherRepository.getWeatherDataByCityName(validLocation)
            }
            assertEquals(response200, mainViewModel.weatherData.value)
            assertEquals(false, mainViewModel.showErrorMessage.value)
            assertEquals("${losAngeles},${countryUs}", mainViewModel.cityNameSearched.value)
        }

    @Test
    fun `Should show error message when location is invalid and 404 status code is received`() =
        runTest {
            // Given
            every {
                weatherRepository.getWeatherDataByCityName(invalidLocation)
            } returns flow {
                emit(response404)
            }
            val dispatcher = StandardTestDispatcher(this.testScheduler)
            Dispatchers.setMain(dispatcher)

            // When
            val mainViewModel = MainViewModel(weatherRepository, dispatcher)
            mainViewModel.refreshWeatherData(invalidLocation)
            advanceUntilIdle()

            // Verify
            every {
                weatherRepository.getWeatherDataByCityName(invalidLocation)
            }
            assertEquals(response404, mainViewModel.weatherData.value)
            assertEquals(true, mainViewModel.showErrorMessage.value)
        }

    @Test
    fun `Should receive weather data successfully when coordinates are valid and status code 200 is received`() =
        runTest {
            // Given
            every {
                weatherRepository.getWeatherDataByCoordinates(coordinates)
            } returns flow {
                emit(response200)
            }
            val dispatcher = StandardTestDispatcher(this.testScheduler)
            Dispatchers.setMain(dispatcher)

            // When
            val mainViewModel = MainViewModel(weatherRepository, dispatcher)
            mainViewModel.refreshWeatherData(coordinates)
            advanceUntilIdle()

            // Verify
            every {
                weatherRepository.getWeatherDataByCoordinates(coordinates)
            }
            assertEquals(response200, mainViewModel.weatherData.value)
            assertEquals(false, mainViewModel.showErrorMessage.value)
            assertEquals("${losAngeles},${countryUs}", mainViewModel.cityNameSearched.value)
        }

    @Test
    fun `Should show error message when coordinates are invalid and 404 status code is received`() =
        runTest {
            // Given
            every {
                weatherRepository.getWeatherDataByCoordinates(coordinates)
            } returns flow {
                emit(response404)
            }
            val dispatcher = StandardTestDispatcher(this.testScheduler)
            Dispatchers.setMain(dispatcher)

            // When
            val mainViewModel = MainViewModel(weatherRepository, dispatcher)
            mainViewModel.refreshWeatherData(coordinates)
            advanceUntilIdle()

            // Verify
            every {
                weatherRepository.getWeatherDataByCoordinates(coordinates)
            }
            assertEquals(response404, mainViewModel.weatherData.value)
            assertEquals(true, mainViewModel.showErrorMessage.value)
        }
}