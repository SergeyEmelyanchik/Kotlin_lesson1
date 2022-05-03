package ru.geekbrains.kotlin_lesson1.view


import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import ru.geekbrains.kotlin_lesson1.R
import ru.geekbrains.kotlin_lesson1.lesson6.MainService
import ru.geekbrains.kotlin_lesson1.lesson6.MyBroadcastReceiver
import ru.geekbrains.kotlin_lesson1.utlis.KEY_BUNDLE_ACTIVITY_MESSAGE
import ru.geekbrains.kotlin_lesson1.utlis.KEY_WAVE
import ru.geekbrains.kotlin_lesson1.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }
        startService(Intent(this,MainService::class.java).apply {
            putExtra(KEY_BUNDLE_ACTIVITY_MESSAGE, "Hello service")
        })

        val receiver = MyBroadcastReceiver()
        //registerReceiver(receiver, IntentFilter(KEY_WAVE))
        registerReceiver(receiver, IntentFilter("android.intent.action.AIRPLANE_MODE"))
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("myaction"))
    }
}