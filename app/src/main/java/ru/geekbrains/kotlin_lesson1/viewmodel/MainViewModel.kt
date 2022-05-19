package ru.geekbrains.kotlin_lesson1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin_lesson1.repository.RepositoryImpl
import ru.geekbrains.kotlin_lesson1.repository.server.ServerRepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val localRepository: RepositoryImpl = RepositoryImpl(),
    private val serverRepository: ServerRepositoryImpl = ServerRepositoryImpl()
) :
    ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherWorld() = getWeather(true)
    fun getWeatherRussia() = getWeather(false)

    private fun getWeather(notFromHere: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading(0))
            if (true) {//(0..10).random() > 5
                val answer =
                    if (notFromHere)
                        localRepository.getWorldWeatherFromLocalStorage()
                    else
                        localRepository.getRussianWeatherFromLocalStorage()
                liveData.postValue(AppState.Success(answer))
            }
            else
                liveData.postValue(AppState.Error(IllegalAccessException("Все плохо")))
        }.start()
    }
}