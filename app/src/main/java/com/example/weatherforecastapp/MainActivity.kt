package com.example.weatherforecastapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecastapp.data.WeatherModel
import com.example.weatherforecastapp.screens.MainCard
import com.example.weatherforecastapp.screens.OutputArea
import com.example.weatherforecastapp.screens.SearchDialogWindow
import org.json.JSONArray
import org.json.JSONObject

val API_KEY = "a09c22c2c90c466390f120302231211"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var daysList = remember {
                mutableStateOf(listOf<WeatherModel>())
            }

            var currentDay = remember {
                mutableStateOf(
                    WeatherModel(
                        "", "",
                        "", "",
                        "", "",
                        "", "",
                    )
                )
            }
            GetData("Kolomna", this, daysList, currentDay)

            var dialogState = remember {
                mutableStateOf(false)
            }
            if (dialogState.value) SearchDialogWindow(dialogState, onSubmit = {
                GetData(it, this, daysList, currentDay)
            })

            Column() {
                MainCard(currentDay, onClickSync = {
                    GetData(
                        "Kolomna", this@MainActivity,
                        daysList, currentDay
                    )
                },
                    onClickSearch = {
                        dialogState.value = true
                    })
                OutputArea(daysList, currentDay)
            }
        }
    }
}

fun GetData(
    city: String, context: Context,
    daysList: MutableState<List<WeatherModel>>,
    currentDay: MutableState<WeatherModel>
) {
    var url =
        "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=${city}&days=10&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val list = GetWeatherByDays(response)
            currentDay.value = list[0]
            daysList.value = list
        },
        {
            Log.d("VolleyLog", "Error: $it")
        }
    )
    queue.add(request)
}

fun GetWeatherByDays(response: String): List<WeatherModel> {
    if (response.isEmpty()) return emptyList()
    val list = ArrayList<WeatherModel>()
    val mainObject = JSONObject(response)
    val city = mainObject.getJSONObject("location").getString("name")
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date"),
                "",
                item.getJSONObject("day")
                    .getJSONObject("condition").getString("text"),
                item.getJSONObject("day")
                    .getJSONObject("condition").getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONArray("hour").toString()
            )
        )
    }
    list[0] = list[0].copy(
        time = mainObject.getJSONObject("current").getString("last_updated"),
        currentTemp = mainObject.getJSONObject("current").getString("temp_c")
    )
    return list
}

fun GetWeatherByHours(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()
    for (i in 0 until hoursArray.length()) {
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("time"),
                item.getString("temp_c"),
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                ""
            )
        )
    }
    return list
}