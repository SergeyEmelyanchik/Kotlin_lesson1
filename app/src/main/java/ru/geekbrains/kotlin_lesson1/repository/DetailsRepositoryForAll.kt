package ru.geekbrains.kotlin_lesson1.repository

import ru.geekbrains.kotlin_lesson1.viewmodel.HistoryViewModel

interface DetailsRepositoryForAll {
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll)
}