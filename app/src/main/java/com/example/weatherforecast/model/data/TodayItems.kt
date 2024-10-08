package com.example.weatherforecast.model.data

import com.example.weatherforecast.R

data class TodayItem(
    val temperature: String,
    val weatherIcon: Int,
    val time: String
)


val todayItems = listOf(
    TodayItem(
        temperature = "29°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "15.00"
    ),
    TodayItem(
        temperature = "26°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "16.00"
    ),
    TodayItem(
        temperature = "24°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "17.00"
    ),
    TodayItem(
        temperature = "24°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "18.00"
    ),
    TodayItem(
        temperature = "24°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "19.00"
    ),
    TodayItem(
        temperature = "23°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "20.00"
    ),
    TodayItem(
        temperature = "22°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "21.00"
    ),
    TodayItem(
        temperature = "21°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "22.00"
    ),
    TodayItem(
        temperature = "20°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "23.00"
    ),
    TodayItem(
        temperature = "20°C",
        weatherIcon = R.drawable.premium_weather_condition_icon_3d,
        time = "00.00"
    ),

)