package ru.geekbrains.kotlin_lesson1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin_lesson1.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryRetrofit2Impl(),
) : ViewModel() {

    fun getLiveData() = liveData
    fun getWeather(city: City){
        repository.getWeatherDetails(city,object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }

        })
    }

    interface Callback{
        fun onResponse(weather: Weather)
    }
}