package ru.geekbrains.kotlin_lesson1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin_lesson1.MyApp
import ru.geekbrains.kotlin_lesson1.repository.*
import ru.geekbrains.kotlin_lesson1.utlis.DEFAULT_VALUE_BOOLEAN_FALSE

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private var repositoryOne: DetailsRepositoryOne = DetailsRepositoryOneRetrofit2Impl(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryOneRoomImpl()
) : ViewModel() {

    fun getLiveData() = liveData
    fun getWeather(city: City) {
        repositoryOne = if (MyApp.isOnline()) {
            DetailsRepositoryOneRetrofit2Impl()
        } else {
            DetailsRepositoryOneRoomImpl()
        }

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
    private fun isOnline(): Boolean {
        return DEFAULT_VALUE_BOOLEAN_FALSE
    }
    interface Callback {
        fun onResponse(weather: Weather)
        fun onFailure(errorMessage: String)
    }
}