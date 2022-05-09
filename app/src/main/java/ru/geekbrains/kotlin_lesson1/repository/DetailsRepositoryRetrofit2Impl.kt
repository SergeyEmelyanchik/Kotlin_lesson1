package ru.geekbrains.kotlin_lesson1.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.kotlin_lesson1.BuildConfig
import ru.geekbrains.kotlin_lesson1.utlis.YANDEX_DOMAIN_HARD_MODE_PART
import ru.geekbrains.kotlin_lesson1.utlis.convertDtoToModel
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsViewModel

class DetailsRepositoryRetrofit2Impl: DetailsRepository {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN_HARD_MODE_PART)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        Thread {
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
        }.start()
        /*weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY,city.lat,city.lon).enqueue(object :Callback<WeatherDTO>{
    override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
        if(response.isSuccessful){
            response.body()?.let {
                callbackDVM.onResponse(convertDtoToModel(it))
            }
        }
    }

    override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {

    }

})*/
}
}