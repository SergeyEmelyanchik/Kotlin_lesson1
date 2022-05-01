package ru.geekbrains.kotlin_lesson1.repository

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {


    fun loadWeather(lat: Double, lon: Double) {

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection =
            (uri.openConnection() as HttpsURLConnection).apply {
                connectTimeout = 1000 // set под капотом
                //val r= readTimeout // get под капотом
                readTimeout = 1000 // set под капотом
                addRequestProperty("X-Yandex-API-Key", "74790723-7847-4fc2-ab50-b41ab6b2d662")
            }

        Thread {
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                //val result = (buffer)
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post {
                    onServerResponseListener.onResponse(weatherDTO)
                }
            } catch (e: JsonSyntaxException) {
                // TODO  HW "что-то пошло не так" Snackbar?

            } finally {
                urlConnection.disconnect()
            }

        }.start()
    }
}