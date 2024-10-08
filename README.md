Weather App
This project is a weather forecast application built using Kotlin and Jetpack Compose. It integrates the WeatherAPI.com API to display current weather conditions and forecasts for different locations.

Features
Current Weather: Display the current weather conditions (temperature, humidity, wind speed, etc.) for a specific location.
Weather Forecast: Show detailed weather forecasts for the next few days.
Search Functionality: Search for weather data by city name or location.
Real-Time Updates: Get real-time weather updates using the WeatherAPI.com API.
Technologies Used
Kotlin: The programming language used for Android development.
Jetpack Compose: For building the modern user interface.
WeatherAPI.com: The weather data provider.
Retrofit: For making HTTP requests to the WeatherAPI.com API.
Coil: For loading weather icons from URLs into the UI.
ViewModel: For managing UI-related data and lifecycle awareness.
Weather API Integration
This app uses the WeatherAPI.com API to fetch weather data. You can find the API documentation here.

API Endpoints
Current Weather: Fetch the current weather for a location.

Example request:
https://api.weatherapi.com/v1/current.json?key=YOUR_API_KEY&q=London
Forecast: Fetch the weather forecast for the next few days.

Example request:
https://api.weatherapi.com/v1/forecast.json?key=YOUR_API_KEY&q=London&days=3


Installation
Clone the repository:
git clone https://github.com/Amos-Tech-code/weather-app.git
Open the project in Android Studio.

Obtain an API key from WeatherAPI.com by signing up.

How to Use
Launch the app to see the current weather for a default location (e.g., Nairobi, Kenya).
Use the search bar to look up weather information for other cities or locations.
View detailed weather forecasts for the next few days.
