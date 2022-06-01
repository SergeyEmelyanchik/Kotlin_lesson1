package ru.geekbrains.kotlin_lesson1.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import ru.geekbrains.kotlin_lesson1.MyApp
import ru.geekbrains.kotlin_lesson1.R
import ru.geekbrains.kotlin_lesson1.lesson10.MapsFragment
import ru.geekbrains.kotlin_lesson1.lesson6.MainService
import ru.geekbrains.kotlin_lesson1.lesson6.MyBroadcastReceiver
import ru.geekbrains.kotlin_lesson1.lesson6.ThreadsFragment
import ru.geekbrains.kotlin_lesson1.utlis.BROADCAST_RECEIVER_CHANNEL_KEY
import ru.geekbrains.kotlin_lesson1.utlis.KEY_SP_FILE_NAME_1
import ru.geekbrains.kotlin_lesson1.utlis.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import ru.geekbrains.kotlin_lesson1.utlis.MAIN_ACTIVITY_KEY
import ru.geekbrains.kotlin_lesson1.view.contentProvider.ContentProviderFragment
import ru.geekbrains.kotlin_lesson1.view.historylist.HistoryWeatherListFragment
import ru.geekbrains.kotlin_lesson1.view.weatherlist.WeatherListFragment
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    companion object {
        private const val NOTIFICATION_ID_LOW = 1
        private const val NOTIFICATION_ID_HIGH = 2
        private const val CHANNEL_ID_LOW = "channel_id_1"
        private const val CHANNEL_ID_HIGH = "channel_id_2"
    }

    private fun push() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(getString(R.string.notification_heading))
            setContentText(getString(R.string.notification_mesage))
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(getString(R.string.notification_heading_two))
            setContentText(getString(R.string.notification_mesage_two))
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Name $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow =
                NotificationChannel(CHANNEL_ID_LOW, channelNameLow, channelPriorityLow).apply {
                    description = channelDescriptionLow
                }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_ID_LOW, notificationBuilderLow.build())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Name $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh =
                NotificationChannel(CHANNEL_ID_HIGH, channelNameHigh, channelPriorityHigh).apply {
                    description = channelDescriptionHigh
                }
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_ID_HIGH, notificationBuilderHigh.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
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
        push()
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
                    .replace(R.id.container, HistoryWeatherListFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            (R.id.actionThreads) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container, ThreadsFragment
                            .newInstance()
                    )
                    .addToBackStack("")
                    .commit()
            }
            (R.id.action_menu_google_maps) -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, MapsFragment())
                    .addToBackStack("")
                    .commit()
            }
            R.id.content_provider ->
                supportFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.container, ContentProviderFragment
                            .newInstance()
                    )
                    .addToBackStack("")
                    .commit()


            (R.id.actionExit) -> {
                exitProcess(0)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}