package ru.geekbrains.kotlin_lesson1.repository

import ru.geekbrains.kotlin_lesson1.repository.dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}