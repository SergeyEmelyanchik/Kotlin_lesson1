package ru.geekbrains.kotlin_lesson1.utlis

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin_lesson1.domain.room.HistoryEntity
import ru.geekbrains.kotlin_lesson1.repository.City
import ru.geekbrains.kotlin_lesson1.repository.Weather
import ru.geekbrains.kotlin_lesson1.repository.dto.FactDTO
import ru.geekbrains.kotlin_lesson1.repository.dto.WeatherDTO
import ru.geekbrains.kotlin_lesson1.repository.getDefaultCity

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(
        0,
        weather.city.cityName,
        weather.temperature,
        weather.feelsLike,
        weather.icon
    )
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon)
    }
}

class MyUtils {
}