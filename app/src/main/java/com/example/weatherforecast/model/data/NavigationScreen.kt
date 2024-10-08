package com.example.weatherforecast.model.data

sealed class NavigationScreen(val route: String) {

    data object HomeScreen: NavigationScreen("home")

    data object ReportScreen: NavigationScreen("full_report")
}