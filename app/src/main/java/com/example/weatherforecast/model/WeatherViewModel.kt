package com.example.weatherforecast.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.api.Constant
import com.example.weatherforecast.api.NetworkResponse
import com.example.weatherforecast.api.RetrofitInstance
import com.example.weatherforecast.api.WeatherModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherApi = RetrofitInstance.weatherApi

//    private val fusedLocationClient: FusedLocationProviderClient =
//        LocationServices.getFusedLocationProviderClient(application)

    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    init {
        getData(city = "Nairobi")
    }

//    fun getCurrentLocation() {
//        try {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    val city = getCityFromLocation(location.latitude, location.longitude)
//                    getData(city)
//                } else {
//                    _weatherResult.value = NetworkResponse.Error("Location not found")
//                }
//            }.addOnFailureListener {
//                _weatherResult.value = NetworkResponse.Error("Failed to get location: ${it.localizedMessage}")
//            }
//        } catch (e: SecurityException) {
//            _weatherResult.value = NetworkResponse.Error("Location permission is not granted")
//        }
//    }

//    private fun getCityFromLocation(latitude: Double, longitude: Double): String {
//        val geocoder = Geocoder(getApplication(), Locale.getDefault())
//        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
//        return if (addresses != null && addresses.isNotEmpty()) {
//            addresses[0].locality ?: "Unknown City"
//        } else {
//            "Unknown City"
//        }
//    }

    fun getData(city: String) {
        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
            try {
                val response = weatherApi.getForecast(
                    apiKey = Constant.apiKey,
                    city = city,
                    days = 7
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    } ?: run {
                        _weatherResult.value = NetworkResponse.Error("No data available")
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data: Invalid Location ${response.message()}")
                }
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    400 -> "Bad request. Please try again."
                    401 -> "Unauthorized access. Check your API key."
                    404 -> "City not found. Please check the city name."
                    500 -> "Server error. Please try again later."
                    else -> "Unexpected error: ${e.message()}"
                }
                _weatherResult.value = NetworkResponse.Error(errorMessage)
            } catch (e: UnknownHostException) {
                _weatherResult.value = NetworkResponse.Error("Connect to the Internet")
            } catch (e: SocketTimeoutException) {
                _weatherResult.value = NetworkResponse.Error("Request timed out. Please try again.")
            } catch (e: IOException) {
                _weatherResult.value = NetworkResponse.Error("Network error. Please check your connection.")
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("An unexpected error occurred: ${e.localizedMessage}")
            }
        }
    }


    //For location suggestions
    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions

    // Function to fetch location suggestions based on user input
    fun fetchLocationSuggestions(query: String) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getLocationSuggestions(apiKey = Constant.apiKey, query = query)
                if (response.isSuccessful) {
                    response.body()?.let { locations ->
                        _suggestions.value = locations.map { it.name } // Adjust according to your API response
                        //Log.d("WeatherViewModel", "Suggestions: ${_suggestions.value}") // Log suggestions
                    } ?: run {
                        _suggestions.value = emptyList() // Handle empty response
                    }
                } else {
                    //Log.e("WeatherViewModel", "Error fetching location suggestions: ${response.raw().request.url}")
                    //Log.e("WeatherViewModel", "Response code: ${response.code()} Response message: ${response.message()}")
                    _suggestions.value = emptyList() // Handle error response
                }
            } catch (e: Exception) {
                //Log.e("WeatherViewModel", "Exception fetching suggestions", e)
                _suggestions.value = emptyList() // Handle any exception
            }
        }
    }
}
