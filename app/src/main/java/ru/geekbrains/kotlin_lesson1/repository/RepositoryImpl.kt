package ru.geekbrains.kotlin_lesson1.repository

class RepositoryImpl: Repository {
    override fun getWorldWeatherFromLocalStorage():List<Weather> {
        return getWorldCities()
    }
    override fun getRussianWeatherFromLocalStorage():List<Weather> {
        return getRussianCities()
    }
}