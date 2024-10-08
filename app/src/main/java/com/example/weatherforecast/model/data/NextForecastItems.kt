package com.example.weatherforecast.model.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherforecast.R
import java.time.DayOfWeek

data class NextForecastItems(
    val dayOfWeek: DayOfWeek,
    val date: String,
    val temperature: String,
    val icon: Int
)

@RequiresApi(Build.VERSION_CODES.O)
val nextForecastItems = listOf(
    NextForecastItems(
        dayOfWeek = DayOfWeek.FRIDAY,
        date = "Nov,04",
        temperature = "29°C",
        icon = R.drawable.premium_weather_condition_icon_3d
    ),
    NextForecastItems(
        dayOfWeek = DayOfWeek.SATURDAY,
        date = "Nov,05",
        temperature = "30°C",
        icon = R.drawable.premium_weather_condition_icon_3d
    ),
    NextForecastItems(
        dayOfWeek = DayOfWeek.SUNDAY,
        date = "Nov,06",
        temperature = "28°C",
        icon = R.drawable.premium_weather_condition_icon_3d
    ),
    NextForecastItems(
        dayOfWeek = DayOfWeek.MONDAY,
        date = "Nov,07",
        temperature = "28°C",
        icon = R.drawable.premium_weather_condition_icon_3d
    ),
    NextForecastItems(
        dayOfWeek = DayOfWeek.TUESDAY,
        date = "Nov,08",
        temperature = "29°C",
        icon = R.drawable.premium_weather_condition_icon_3d
    ),
    NextForecastItems(
        dayOfWeek = DayOfWeek.WEDNESDAY,
        date = "Nov,09",
        temperature = "30°C",
        icon = R.drawable.premium_weather_condition_icon_3d
    ),
    NextForecastItems(
        dayOfWeek = DayOfWeek.THURSDAY,
        date = "Nov,10",
        temperature = "29°C",
        icon = R.drawable.premium_weather_condition_icon_3d
    ),
)