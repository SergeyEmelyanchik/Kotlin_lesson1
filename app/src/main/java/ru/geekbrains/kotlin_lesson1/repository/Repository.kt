package ru.geekbrains.kotlin_lesson1.repository

import ru.geekbrains.kotlin_lesson1.repository.Weather

interface Repository {
    fun getWorldWeatherFromLocalStorage(): List<Weather>
    fun getRussianWeatherFromLocalStorage(): List<Weather>
}