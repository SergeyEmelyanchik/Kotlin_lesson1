package ru.geekbrains.kotlin_lesson1.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper

import ru.geekbrains.kotlin_lesson1.R
import ru.geekbrains.kotlin_lesson1.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }
        val looperNotNullable: Looper = getMainLooper()
        val looperNullable: Looper? = getMainLooper()
    }
}