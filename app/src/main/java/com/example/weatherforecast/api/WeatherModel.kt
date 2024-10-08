package com.example.weatherforecast.api

data class WeatherModel(
    val current: Current,
    val location: Location,
    val forecast: Forecast
)





//For Hourly Implementation

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val hour: List<Hour>,
    val day: Day
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val avgtemp_c: Double,
    val condition: Condition
)

data class Hour(
    val time: String,  // The time of the forecast
    val temp_c: Double,  // Temperature in Celsius
    val condition: Condition,  // Weather condition (e.g., Sunny, Rain)
    val wind_kph: Double,  // Wind speed in km/h
    val precip_mm: Double,  // Precipitation in mm
    val humidity: Int  // Humidity percentage
)

data class LocationResponse(
    val id: Int,                // Unique identifier for the location
    val name: String,           // Name of the city/location
    val region: String?,        // Region (can be null if not applicable)
    val country: String,        // Country name
    val lat: Double,            // Latitude of the location
    val lon: Double,            // Longitude of the location
    val url: String             // URL for more information about the location
)