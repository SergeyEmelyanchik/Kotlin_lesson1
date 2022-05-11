package ru.geekbrains.kotlin_lesson1.view

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.kotlin_lesson1.MyApp
import ru.geekbrains.kotlin_lesson1.R
import ru.geekbrains.kotlin_lesson1.lesson6.MainService
import ru.geekbrains.kotlin_lesson1.lesson6.MyBroadcastReceiver
import ru.geekbrains.kotlin_lesson1.lesson6.ThreadsFragment
import ru.geekbrains.kotlin_lesson1.utlis.BROADCAST_RECEIVER_CHANNEL_KEY
import ru.geekbrains.kotlin_lesson1.utlis.KEY_SP_FILE_NAME_1
import ru.geekbrains.kotlin_lesson1.utlis.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import ru.geekbrains.kotlin_lesson1.utlis.MAIN_ACTIVITY_KEY
import ru.geekbrains.kotlin_lesson1.view.historylist.HistoryWeatherListFragment
import ru.geekbrains.kotlin_lesson1.view.weatherlist.WeatherListFragment
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.mainContainer,
                WeatherListFragment.newInstance()
            ).commit()
        }

        startService(Intent(this, MainService::class.java).apply {
            putExtra(MAIN_ACTIVITY_KEY, "Привет сервис")
        })
        val sp = getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)

        val editor = sp.edit()
        editor.putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, true)
        editor.apply()


        val defaultValueIsRussian = true
        sp.getBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, defaultValueIsRussian)

        val receiver = MyBroadcastReceiver()
        registerReceiver(receiver, IntentFilter(BROADCAST_RECEIVER_CHANNEL_KEY))
/*        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter(BROADCAST_RECEIVER_CHANNEL_KEY))*/
        Thread { MyApp.getHistoryDAO().getAll() }.start()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            (R.id.actionHistory) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, HistoryWeatherListFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            (R.id.actionThreads) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, ThreadsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            (R.id.actionExit) -> {
                exitProcess(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}