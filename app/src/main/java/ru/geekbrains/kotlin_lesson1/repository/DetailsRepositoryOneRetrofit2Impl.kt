package ru.geekbrains.kotlin_lesson1.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.kotlin_lesson1.BuildConfig
import ru.geekbrains.kotlin_lesson1.utlis.LOG_KEY
import ru.geekbrains.kotlin_lesson1.utlis.YANDEX_DOMAIN_HARD_MODE_PART
import ru.geekbrains.kotlin_lesson1.utlis.convertDtoToModel
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsViewModel

class DetailsRepositoryOneRetrofit2Impl: DetailsRepositoryOne {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN_HARD_MODE_PART)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        Thread {
            try {
                val responseA =
                    weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon).execute()
                if (responseA.isSuccessful) {
                    responseA.body()?.let {
                        callbackMy.onResponse(convertDtoToModel(it).apply {
                            this.city.cityName = city.cityName
                        })
                    }
                } else {
                    callbackMy.onFailure("ВНИМАНИЕ ОШИБОЧНЫЙ РЕЗУЛЬТАТ ${responseA.errorBody().toString()}")
        }
            } catch (e: JsonSyntaxException) {
                Log.d(LOG_KEY,"e.message ${e.message.toString()} ][ ${e.toString()}")
                callbackMy.onFailure(e.message.toString())
            }
        }.start()
}
}