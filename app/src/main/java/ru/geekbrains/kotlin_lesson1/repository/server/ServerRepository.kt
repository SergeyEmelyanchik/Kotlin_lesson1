package ru.geekbrains.kotlin_lesson1.repository.server

import ru.geekbrains.kotlin_lesson1.repository.Weather

interface ServerRepository {
    fun getWeatherFromServer(): Weather
}