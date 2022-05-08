package ru.geekbrains.kotlin_lesson1.repository.weather


import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.geekbrains.kotlin_lesson1.BuildConfig
import ru.geekbrains.kotlin_lesson1.repository.OnServerResponse
import ru.geekbrains.kotlin_lesson1.repository.OnServerResponseListener
import ru.geekbrains.kotlin_lesson1.repository.dto.WeatherDTO
import ru.geekbrains.kotlin_lesson1.utlis.*
import ru.geekbrains.kotlin_lesson1.viewmodel.ResponseState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherLoader(
    private val onServerResponseListener: OnServerResponse,
    private val onErrorListener: OnServerResponseListener
) {
    fun loadWeather(lat: Double, lon: Double) {
        //val urlText = $YANDEX_DOMAIN_PART$YANDEX_ENDPOINT$LAT_KEY$=lat$LON_KEY=$lon
        val urlText = "$YANDEX_DOMAIN_HARD_MODE_PART$YANDEX_ENDPOINT$LAT_KEY=$lat&$LON_KEY=$lon"
        val uri = URL(urlText)
        //val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
        val urlConnection: HttpURLConnection = (uri.openConnection() as HttpURLConnection).apply {
           connectTimeout = CONNECT_TIMEOUT
            readTimeout = READ_TIMEOUT
            addRequestProperty(
                YANDEX_API_KEY,
                BuildConfig.WEATHER_API_KEY
            )
        }
        Thread {
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage
                Log.d(LOG_KEY, "responseCode: $responseCode   responseMessage: $responseMessage")
                if (responseCode in SERVER_SIDE) {
                    onErrorListener.onError(ResponseState.ErrorOnServerSide("responseCode: $responseCode   responseMessage: $responseMessage"))
                } else if (responseCode in CLIENT_SIDE) {
                    onErrorListener.onError(ResponseState.ErrorOnClientSide("responseCode: $responseCode   responseMessage: $responseMessage"))
                } else if (responseCode in RESPONSEOK) {
                    val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO: WeatherDTO = Gson().fromJson(
                        buffer,
                        WeatherDTO::class.java
                    )
                    Handler(Looper.getMainLooper()).post {
                        onServerResponseListener.onResponse(weatherDTO)
                    }
                }
            } catch (e: JsonSyntaxException) {
                onErrorListener.onError(ResponseState.ErrorInJSONConversion(e.toString()))
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}