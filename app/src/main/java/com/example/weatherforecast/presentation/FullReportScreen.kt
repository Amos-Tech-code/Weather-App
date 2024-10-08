package com.example.weatherforecast.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weatherforecast.api.ForecastDay
import com.example.weatherforecast.api.Hour
import com.example.weatherforecast.api.NetworkResponse
import com.example.weatherforecast.api.WeatherModel
import com.example.weatherforecast.model.WeatherViewModel
import com.example.weatherforecast.model.data.NavigationScreen
import com.example.weatherforecast.ui.theme.brightBlue
import com.example.weatherforecast.ui.theme.darkBlack
import com.example.weatherforecast.ui.theme.darkBlue1
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FullReportScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel,

    ) {
    // Observe weather result from ViewModel
    val weatherResult = weatherViewModel.weatherResult.observeAsState()
    //val hours: MutableList<Hour> = mutableListOf()

    Scaffold(
        topBar = {
            ToBar(navController = navController)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBlue1)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            when(val result = weatherResult.value) {
                is NetworkResponse.Error -> {
                    Column {
                        Text(text = result.message, color = Color.Red, textAlign = TextAlign.Center)
                        Button(
                            onClick = { weatherViewModel.getData(city = "Nairobi") },
                            colors = ButtonDefaults.buttonColors(containerColor = brightBlue)
                        ) {
                            Text(text = "Retry", color = Color.White)
                        }
                    }
                }

                NetworkResponse.Loading -> {
                    CircularProgressIndicator(
                        color = brightBlue
                    )
                }

                is NetworkResponse.Success -> {
                    TodayReportFullScreen(data = result.data)
                }

                else -> {}
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayReportFullScreen(
    data: WeatherModel
) {
    Column {
        // Extract the first day's hourly data
        val forecast = data.forecast.forecastday.firstOrNull()
        forecast?.hour?.let { hourlyData ->
            TodayReport2( hours = hourlyData)
        } ?: run {
            Text(
                text = "No forecast data available.",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        NextForecast(nextForecastItems = data.forecast.forecastday)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayReport2(
    hours: List<Hour>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
                Text(
                    text = getCurrentDate(),
                    fontSize = 19.sp,
                    color = brightBlue
                )
            }
        }

        LazyRow {
            items(hours.size) { index ->
                val hour = hours[index]
                TodayItems2(hour)
            }
        }
}


@Composable
fun TodayItems2(hour: Hour) {

//    val item = todayItems[index]
//
//    var lastPaddingEnd = 0.dp
//    if (index == todayItems.size - 1) {
//        lastPaddingEnd = 16.dp
//    }

    Box(
        modifier = Modifier.padding(start = 10.dp, /*end = lastPaddingEnd,*/ top = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(darkBlack)
                .height(150.dp)
                .padding(13.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = hour.temp_c.toString() + "° C",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            AsyncImage(
                model = "https:${hour.condition.icon}".replace("64x64", "128x128"),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = hour.time.split(" ")[1],
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextForecast(
    nextForecastItems: List<ForecastDay>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Next Forecast",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        LazyColumn {
            items(nextForecastItems.size) { index ->
                NextForecastItem(nextForecastItems[index])
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextForecastItem(
    forecastDay: ForecastDay
) {

    // Extract relevant data from the forecast day
    val dayOfWeek = getDayOfWeek(forecastDay.date) // Convert date to day of the week
    val date = formatDateToMonthDay(forecastDay.date) // Format date to "October 08"
    val temperature = "${forecastDay.day.avgtemp_c}°C" // Make sure `day` and `avgtemp_c` exist
    val iconUrl = forecastDay.day.condition.icon // Ensure `condition` and `icon` exist within `day`

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 6.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(darkBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dayOfWeek,
                    color = Color.White,
                    fontSize = 17.sp
                )
                Text(
                    text = date,
                    color = Color.White,
                    fontSize = 19.sp
                )
            }

            Column {
                Text(
                    text = temperature,
                    color = Color.White,
                    fontSize = 27.sp
                )
            }

            Column {
                AsyncImage(
                    modifier = Modifier
                        .size(35.dp),
                    model = "https:${iconUrl}".replace("64x64", "128x128"),
                    contentDescription = "Condition icon"
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToBar(
    navController: NavController
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = darkBlue1
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(NavigationScreen.HomeScreen.route) {
                        popUpTo(NavigationScreen.ReportScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        title = {
            Text(
                text = "Forecast Report",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        },

    )
}



@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfWeek(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateToMonthDay(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    return date.format(DateTimeFormatter.ofPattern("MMMM dd"))
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    // Format the current date in "MMM dd, yyyy" format
    return LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
}