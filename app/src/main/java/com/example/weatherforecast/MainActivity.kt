package com.example.weatherforecast

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecast.model.WeatherViewModel
import com.example.weatherforecast.model.data.NavigationScreen
import com.example.weatherforecast.presentation.FullReportScreen
import com.example.weatherforecast.presentation.HomeScreen
import com.example.weatherforecast.ui.theme.WeatherForecastTheme
import com.example.weatherforecast.ui.theme.darkBlue1
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    private val weatherViewModel by viewModels<WeatherViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request location permissions
        //requestLocationPermission()

        setContent {
            WeatherForecastTheme {

                SetBarContentToWhite(color = darkBlue1)

                Surface(modifier = Modifier.fillMaxSize()) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = NavigationScreen.HomeScreen.route,
                        enterTransition = {
                            slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(700))
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(700))
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn(animationSpec = tween(700))
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut(animationSpec = tween(700))
                        }
                    ) {
                        composable(NavigationScreen.HomeScreen.route) {
                            HomeScreen(
                                navController = navController,
                                weatherViewModel = weatherViewModel
                            )
                        }

                        composable(NavigationScreen.ReportScreen.route) {
                            FullReportScreen(
                                navController = navController,
                                weatherViewModel = weatherViewModel,
                            )
                        }
                    }
                }
            }
        }
    }

//    private fun requestLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
//        } else {
//            // Permission is already granted, proceed with your location logic
//            weatherViewModel.getCurrentLocation()
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>, // Changed here to match superclass signature
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission was granted, proceed with your location logic
//                weatherViewModel.getCurrentLocation()
//            } else {
//                // Permission denied, show an explanation or handle accordingly.
//                // You might want to show a dialog or a Toast message here.
//            }
//        }
//    }

    @Composable
    private fun SetBarContentToWhite(color: Color) {
        val systemUiController = rememberSystemUiController()

        LaunchedEffect(Unit) {
            systemUiController.setSystemBarsColor(
                color = color,
                darkIcons = false // Adjust based on your design needs.
            )
        }
    }

//    companion object {
//        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001 // Unique request code for location permission.
//    }
}