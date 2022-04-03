package ru.geekbrains.kotlin_lesson1.viewmodel

sealed class AppState {
    object Loading:AppState()
    data class Success(val weatherData:Any):AppState(){
        fun test(){}
    }
    data class Error(val error:Throwable):AppState()
}