package com.botime.awesomeweather.di

import com.botime.awesomeweather.api.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideWeatherService(
        // Potential dependencies of this type
    ): WeatherService {
        return provideServiceInternal(DEFAULT_BASE_URL) as WeatherService
    }

    private fun provideServiceInternal(baseUrl: String): Any {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(logger)
        val converterFactory = GsonConverterFactory.create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(converterFactory)
            .build()
            .create(baseUrl2ServiceClassMap[baseUrl] ?: WeatherService::class.java)
    }

    companion object {
        private const val DEFAULT_BASE_URL = "https://api.openweathermap.org/"
        private val baseUrl2ServiceClassMap = mapOf(
            DEFAULT_BASE_URL to WeatherService::class.java,
        )
    }
}