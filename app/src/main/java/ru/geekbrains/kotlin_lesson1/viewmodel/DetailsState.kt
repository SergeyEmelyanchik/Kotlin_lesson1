package ru.geekbrains.kotlin_lesson1.viewmodel

import ru.geekbrains.kotlin_lesson1.repository.Weather

sealed class DetailsState {
    object Loading:DetailsState()
    data class Success(val weather: Weather):DetailsState()
    data class Error(val error:Throwable):DetailsState()
}