package ru.geekbrains.kotlin_lesson1.repository

import ru.geekbrains.kotlin_lesson1.viewmodel.ResponseState

fun interface OnServerResponseListener {
    fun onError(error: ResponseState)
}