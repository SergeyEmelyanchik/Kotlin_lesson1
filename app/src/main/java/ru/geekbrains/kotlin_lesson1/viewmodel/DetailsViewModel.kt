package ru.geekbrains.kotlin_lesson1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin_lesson1.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryOne: DetailsRepositoryOne = DetailsRepositoryOneRetrofit2Impl(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryOneRoomImpl()
) : ViewModel() {

    fun getLiveData() = liveData
    fun getWeather(city: City) {
        repositoryOne.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
            }

            override fun onFailure(errorMessage: String) {
                liveData.postValue(DetailsState.Error(Throwable(errorMessage)))
            }

        })
    }

    interface Callback {
        fun onResponse(weather: Weather)
        fun onFailure(errorMessage: String)
    }
}