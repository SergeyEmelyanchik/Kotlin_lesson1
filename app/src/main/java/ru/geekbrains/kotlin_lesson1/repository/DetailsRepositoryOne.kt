package ru.geekbrains.kotlin_lesson1.repository

import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsViewModel

interface DetailsRepositoryOne {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}