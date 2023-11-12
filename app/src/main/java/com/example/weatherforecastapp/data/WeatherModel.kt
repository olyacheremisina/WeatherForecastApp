package com.example.weatherforecastapp.data


data class WeatherModel(
    val city: String,
    val time: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val maxtemp: String,
    val mintemp: String,
    val hours: String,
)
