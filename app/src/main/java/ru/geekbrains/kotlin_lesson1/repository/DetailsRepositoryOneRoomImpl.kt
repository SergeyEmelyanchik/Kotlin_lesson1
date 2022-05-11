package ru.geekbrains.kotlin_lesson1.repository

import ru.geekbrains.kotlin_lesson1.MyApp
import ru.geekbrains.kotlin_lesson1.utlis.convertHistoryEntityToWeather
import ru.geekbrains.kotlin_lesson1.utlis.convertWeatherToEntity
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsViewModel
import ru.geekbrains.kotlin_lesson1.viewmodel.HistoryViewModel

class DetailsRepositoryOneRoomImpl : DetailsRepositoryOne, DetailsRepositoryForAll,
    DetailsRepositoryAdd {
    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val weatherList =
            convertHistoryEntityToWeather(MyApp.getHistoryDAO().getHistoryForCity(city.cityName))
        callback.onResponse(weatherList.last())
    }

    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        Thread {
            callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDAO().getAll()))
        }.start()
    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDAO().insert(convertWeatherToEntity(weather))
    }

}