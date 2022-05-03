package ru.geekbrains.kotlin_lesson1.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.geekbrains.kotlin_lesson1.BuildConfig
import ru.geekbrains.kotlin_lesson1.repository.dto.WeatherDTO
import ru.geekbrains.kotlin_lesson1.utlis.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailsService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work DetailsService")
        intent?.let {
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            Log.d("@@@", "work DetailsService $lat $lon")


            val urlText =
                "$YANDEX_DOMAIN${YANDEX_PATH}lat=$lat&lon=$lon"
            //val urlText = "$YANDEX_DOMAIN_HARD_MODE${YANDEX_PATH}lat=$lat&lon=$lon"
            val uri = URL(urlText)

            //val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply { для ленивых
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000 // set под капотом
                    //val r= readTimeout // get под капотом
                    readTimeout = 1000 // set под капотом
                    addRequestProperty(
                        "X-Yandex-API-Key",
                        BuildConfig.WEATHER_API_KEY
                    )
                }

            val responseCode = urlConnection.responseCode
            val responseMessage = urlConnection.responseMessage
            val serverSide = 500..599
            val clientSide = 400..499
            val responseOk = 200..299
            when (responseCode) {
                in serverSide -> {
                    Log.e("@@@", "$responseCode + $responseMessage")
                }
                in clientSide -> {
                    Log.e("@@@", "$responseCode + $responseMessage")
                }
                in responseOk -> {
                    Log.e("@@@", "$responseCode + $responseMessage")
                }
            }

            val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
            try {
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)

                val message = Intent(KEY_WAVE_SERVICE_BROADCAST)
                message.putExtra(
                    KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO
                )
                //sendBroadcast(message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(message)
            } catch (e: JsonSyntaxException) {

            } finally {
                urlConnection.disconnect()
            }
        }
    }
}







