package ru.geekbrains.kotlin_lesson1.repository


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.geekbrains.kotlin_lesson1.repository.dto.WeatherDTO
import ru.geekbrains.kotlin_lesson1.utlis.LAT_KEY
import ru.geekbrains.kotlin_lesson1.utlis.LON_KEY
import ru.geekbrains.kotlin_lesson1.utlis.YANDEX_API_KEY
import ru.geekbrains.kotlin_lesson1.utlis.YANDEX_ENDPOINT

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT)
    fun getWeather(
        @Header(YANDEX_API_KEY) apiKey: String,
        @Query(LAT_KEY) lat: Double,
        @Query(LON_KEY) lon: Double
    ): Call<WeatherDTO>
}