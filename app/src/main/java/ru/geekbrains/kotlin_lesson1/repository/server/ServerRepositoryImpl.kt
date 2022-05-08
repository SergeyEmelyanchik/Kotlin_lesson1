package ru.geekbrains.kotlin_lesson1.repository.server

import ru.geekbrains.kotlin_lesson1.repository.Weather
import ru.geekbrains.kotlin_lesson1.utlis.SERVER_REPOSITORY_IMPL_SLEEP_TIME

class ServerRepositoryImpl: ServerRepository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(SERVER_REPOSITORY_IMPL_SLEEP_TIME)
        return Weather()
    }
}
