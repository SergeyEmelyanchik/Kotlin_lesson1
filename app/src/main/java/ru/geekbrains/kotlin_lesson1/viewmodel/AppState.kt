package ru.geekbrains.kotlin_lesson1.viewmodel

import ru.geekbrains.kotlin_lesson1.repository.Weather

sealed class AppState {
    object Loading:AppState()
    data class Success(val weatherData: Weather):AppState(){
        fun test(){}
    }
    data class Error(val error:Throwable):AppState()
}