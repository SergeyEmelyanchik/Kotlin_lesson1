package ru.geekbrains.kotlin_lesson1.viewmodel

sealed class ResponseState {
    data class ErrorOnServerSide(val errorMessage: String) : ResponseState()
    data class ErrorOnClientSide(val errorMessage: String) : ResponseState()
    data class ErrorInJSONConversion(val errorMessage: String) : ResponseState()

}