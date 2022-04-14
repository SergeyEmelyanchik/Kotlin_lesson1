package ru.geekbrains.kotlin_lesson1.view.weatherlist

import ru.geekbrains.kotlin_lesson1.repository.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}