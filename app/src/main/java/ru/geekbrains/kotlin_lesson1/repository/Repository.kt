package ru.geekbrains.kotlin_lesson1.repository

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWorldWeatherFromLocalStorage(): List<Weather>
    fun getRussianWeatherFromLocalStorage(): List<Weather>
}