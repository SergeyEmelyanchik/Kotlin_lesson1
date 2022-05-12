package ru.geekbrains.kotlin_lesson1

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.Room
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.kotlin_lesson1.domain.room.HistoryDAO
import ru.geekbrains.kotlin_lesson1.domain.room.MyDB
import ru.geekbrains.kotlin_lesson1.repository.WeatherAPI
import ru.geekbrains.kotlin_lesson1.utlis.YANDEX_DOMAIN_HARD_MODE_PART

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var dataBase: MyDB? = null
        private var appContext: MyApp? = null
        fun getHistoryDAO(): HistoryDAO {
            if (dataBase == null) {
                if (appContext != null) {
                    dataBase = Room
                        .databaseBuilder(appContext!!, MyDB::class.java, "history_response_table")
                        .build()
                } else {
                    throw IllegalStateException("Что-то пошло не так! appContext пуст")
                }
            }
            return dataBase!!.historyDAO()
        }

        fun getWeatherAPI(): WeatherAPI {
            return Retrofit.Builder().apply {
                baseUrl(YANDEX_DOMAIN_HARD_MODE_PART)
                addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
            }.build().create(WeatherAPI::class.java)
        }

        fun isOnline(): Boolean {
            val connectivityManager =
                appContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) -> return false
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) -> return true
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) -> return true
                }
            }
            return false
        }

    }
}