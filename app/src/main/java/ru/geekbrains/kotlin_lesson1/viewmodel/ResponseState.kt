package ru.geekbrains.kotlin_lesson1.viewmodel

import ru.geekbrains.kotlin_lesson1.repository.Weather

sealed class ResponseState {
    object Error1:ResponseState()
    data class Error2(val weatherList:List<Weather>):ResponseState(){
        fun test(){}
    }
    data class Error3(val error:Throwable):ResponseState()
}