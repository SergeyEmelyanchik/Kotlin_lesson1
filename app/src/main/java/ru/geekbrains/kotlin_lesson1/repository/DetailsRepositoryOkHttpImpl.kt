package ru.geekbrains.kotlin_lesson1.repository

import com.google.gson.Gson
import ru.geekbrains.kotlin_lesson1.BuildConfig
import ru.geekbrains.kotlin_lesson1.repository.dto.WeatherDTO
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.geekbrains.kotlin_lesson1.utlis.*

class DetailsRepositoryOkHttpImpl:DetailsRepository {
    override fun getWeatherDetails(city: City,callback: DetailsViewModel.Callback) {
        val client = OkHttpClient()
        val requestBuilder = Request.Builder()
        requestBuilder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        requestBuilder.url("$YANDEX_DOMAIN_HARD_MODE_PART${YANDEX_ENDPOINT}lat=${city.lat}&lon=${city.lon}")//url("{$YANDEX_DOMAIN_PART}{$YANDEX_ENDPOINT}{$LAT_KEY}={$city.lat}&{$LON_KEY}={$city.lon}")
        val request = requestBuilder.build()
        val call = client.newCall(request)
        Thread{
            val response = call.execute()
            if(response.isSuccessful){
                val serverResponse = response.body()!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse,WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                callback.onResponse(weather)
            }
        }.start()
    }
}