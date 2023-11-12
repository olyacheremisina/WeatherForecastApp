package com.example.weatherforecastapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherforecastapp.GetWeatherByHours
import com.example.weatherforecastapp.data.WeatherModel
import com.example.weatherforecastapp.ui.theme.MainBackground
import com.example.weatherforecastapp.ui.theme.OutputCardBackground
import com.example.weatherforecastapp.ui.theme.TextColor

@Composable
fun OutputArea(
    daysList: MutableState<List<WeatherModel>>,
    currentDay: MutableState<WeatherModel>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(1) {
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .fillParentMaxWidth()
                ) {
                    HoursForecast(currentDay)
                }
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .fillParentMaxWidth()
                ) {
                    DaysForecast(daysList)
                }
            }
        }
    }
}

@Composable
fun DaysForecast(daysList: MutableState<List<WeatherModel>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .clip(RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            colors = CardDefaults.cardColors(containerColor = OutputCardBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Прогноз по дням", color = TextColor,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(OutputCardBackground)
                ) {
                    itemsIndexed(daysList.value) { _, item ->
                        ItemCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun HoursForecast(currentDay: MutableState<WeatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = OutputCardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Прогноз по часам", color = TextColor,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(GetWeatherByHours(currentDay.value.hours)) { _, item ->
                    ItemCard(item)
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: WeatherModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MainBackground
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(text = item.time, color = TextColor)
                Text(text = item.condition, color = TextColor)
            }
            Text(
                text = item.currentTemp.ifEmpty { "${item.maxtemp}°C/${item.mintemp}°C" },
                color = TextColor, fontSize = 25.sp
            )
            AsyncImage(
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 10.dp),
                model = "https:" + item.icon,
                contentDescription = "State icon"
            )
        }
    }
}