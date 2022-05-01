package ru.geekbrains.kotlin_lesson1.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import ru.geekbrains.kotlin_lesson1.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin_lesson1.repository.OnServerResponse
import ru.geekbrains.kotlin_lesson1.repository.Weather
import ru.geekbrains.kotlin_lesson1.repository.WeatherDTO
import ru.geekbrains.kotlin_lesson1.repository.WeatherLoader
import ru.geekbrains.kotlin_lesson1.utlis.KEY_BUNDLE_WEATHER


class DetailsFragment : Fragment(), OnServerResponse {


    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            //Thread{
            WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
            //}.start()
        }
    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            with(weather) {
                cityName.text = currentCityName
                temperatureValue.text = weather.factDTO.temperature.toString()
                feelsLikeValue.text = weather.factDTO.feelsLike.toString()
                cityCoordinates.text = "${weather.infoDTO.lat} ${weather.infoDTO.lon}"
            }
        }
        mainView.showSnackBar("Все работает!")
    }

    private fun View.showSnackBar(it: String) {
        Snackbar.make(this, it, Snackbar.LENGTH_SHORT).show()
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}