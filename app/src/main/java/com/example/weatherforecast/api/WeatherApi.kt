package com.example.weatherforecast.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: Int,  // Number of days for forecast
        @Query("aqi") aqi: String = "no",  // Air Quality Index
        @Query("alerts") alerts: String = "no"  // Weather alerts
    ): Response<WeatherModel>

    @GET("/v1/search.json")
    suspend fun getLocationSuggestions(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): Response<List<LocationResponse>>
}