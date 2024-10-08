package com.example.weatherforecast.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weatherforecast.R
import com.example.weatherforecast.api.Hour
import com.example.weatherforecast.api.NetworkResponse
import com.example.weatherforecast.api.WeatherModel
import com.example.weatherforecast.model.WeatherViewModel
import com.example.weatherforecast.model.data.NavigationScreen
import com.example.weatherforecast.ui.theme.brightBlue
import com.example.weatherforecast.ui.theme.darkBlack
import com.example.weatherforecast.ui.theme.darkBlue1


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    weatherViewModel: WeatherViewModel,
) {
    val weatherResult = weatherViewModel.weatherResult.observeAsState()

    var city by remember { mutableStateOf("") }
    val suggestions by weatherViewModel.suggestions.observeAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    val filteredSuggestions = suggestions.filter { it.contains(city, ignoreCase = true) }
    var isFocused by remember { mutableStateOf(false) }
    var isSearchVisible by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopBar(
                city = city,
                onCityChange = {
                    city = it
                    weatherViewModel.fetchLocationSuggestions(it)
                },
                onSearch = {
                    weatherViewModel.getData(city)
                    keyboardController?.hide()
                    isFocused = false // Hide suggestions after search
                },
                isFocused = isFocused,
                isSearchVisible = isSearchVisible,
                onFocusChanged = { isFocused = it },
                onToggleSearchVisibility = { isSearchVisible = it }
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBlue1)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Display suggestions below TopAppBar
            if (filteredSuggestions.isNotEmpty() && isFocused) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                ) {
                    Column {
                        filteredSuggestions.forEach { suggestion ->
                            Text(
                                text = suggestion,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        city = suggestion
                                        weatherViewModel.getData(city)
                                        keyboardController?.hide()
                                        isFocused = false // Hide suggestions after search
                                    }
                                    .padding(8.dp),
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            when (val result = weatherResult.value) {
                is NetworkResponse.Error -> {
                    Column {
                        Text(text = result.message, color = Color.Red, textAlign = TextAlign.Center)
                        Button(
                            onClick = { weatherViewModel.getData(city = city) },
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
                    WeatherDataScreen(
                        navController = navController,
                        data = result.data,
                    )
                }
                else -> {}
            }
        }
    }
}





@Composable
fun WeatherDataScreen(
    navController: NavController,
    data: WeatherModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.LocationOn,
            contentDescription = "Location icon",
            tint = brightBlue,
            modifier = Modifier.size(35.dp)
        )
        Text(
            text = data.location.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            text = data.location.country,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }

    ImageIcon(data = data)

    WeatherStatus(data = data)

    // Extract the first day's hourly data
    val forecast = data.forecast.forecastday.firstOrNull()
    forecast?.hour?.let { hourlyData ->
        TodayReport(navController = navController, hours = hourlyData)
    } ?: run {
        Text(
            text = "No forecast data available.",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }
}




@Composable
fun ImageIcon(
    modifier: Modifier = Modifier,
    data: WeatherModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .size(150.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition icon"
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${data.current.temp_c}° C",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = data.current.condition.text,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}


@Composable
fun WeatherStatus(
    data: WeatherModel
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(darkBlack)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.precipitation_removebg_preview),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    text = data.current.precip_mm + "mm",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Precipitation",
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Opacity,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
                Text(
                    text = data.current.humidity,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Humidity",
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Air,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
                Text(
                    text = data.current.wind_kph + "Km/h",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "WindSpeed",
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }
    }
}



@Composable
fun TodayReport(
    navController: NavController,
    hours: List<Hour>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
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
            TextButton(onClick = { navController.navigate(NavigationScreen.ReportScreen.route) }) {
                Text(
                    text = "View full report",
                    fontSize = 19.sp,
                    color = brightBlue
                )
            }
        }

        LazyRow {
            items(hours.size) { index ->
                val hour = hours[index]
                TodayItems(hour)
            }
        }
    }
}


@Composable
fun TodayItems(hour: Hour) {

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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    city: String,
    onCityChange: (String) -> Unit,
    onSearch: () -> Unit,
    isFocused: Boolean,
    isSearchVisible: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    onToggleSearchVisibility: (Boolean) -> Unit
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBlue1),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = isSearchVisible,
                    enter = androidx.compose.animation.slideInHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ),
                    exit = androidx.compose.animation.slideOutHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetX = { fullWidth -> fullWidth }
                    )
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .onFocusChanged { focusState ->
                                onFocusChanged(focusState.isFocused)
                            },
                        value = city,
                        onValueChange = onCityChange,
                        label = { Text("Search for any location") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                onSearch()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = brightBlue,
                            unfocusedBorderColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White
                        )
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { onToggleSearchVisibility(!isSearchVisible) }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search for any location",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    )
}


