package ru.geekbrains.kotlin_lesson1.repository

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}