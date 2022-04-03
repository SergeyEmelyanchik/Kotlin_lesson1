package ru.geekbrains.kotlin_lesson1.repository

import android.util.Log

data class MyDataClass(var weather: Int, var town: String)


object MyTestObject {

    fun getMyTest() = "Привет"

    fun getTestForEach() {
        val list = listOf(1, 2, 3, 4)
        list.forEach {
            Log.d("*****", "$it Привет")
        }
    }

}
