package ru.geekbrains.kotlin_lesson1.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import ru.geekbrains.kotlin_lesson1.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin_lesson1.repository.*
import ru.geekbrains.kotlin_lesson1.repository.dto.WeatherDTO
import ru.geekbrains.kotlin_lesson1.utlis.*
import ru.geekbrains.kotlin_lesson1.viewmodel.ResponseState


class DetailsFragment : Fragment(), OnServerResponse, OnServerResponseListener {
    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireContext().unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.let { intent ->
                intent.getParcelableExtra<WeatherDTO>(SERVICE_BROADCAST_WEATHER_KEY)?.let {
                    onResponse(it)
                }
            }
        }
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

        requireContext().registerReceiver(
            receiver,
            IntentFilter(BROADCAST_RECEIVER_CHANNEL_WEATHER_KEY)
        )

        requireArguments().getParcelable<Weather>(BUNDLE_WEATHER_KEY)?.let {
            currentCityName = it.city.cityName
            /* WeatherLoader(this@DetailsFragment, this@DetailsFragment).loadWeather(
                it.city.lat,
                it.city.lon
            )*/

            requireActivity().startService(
                Intent(
                    requireContext(),
                    DetailsService::class.java
                ).apply {
                    putExtra(LON_KEY, it.city.lon)
                    putExtra(LAT_KEY, it.city.lat)
                })

        }
    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            cityName.text = currentCityName
            temperatureValue.text = weather.factDTO.temperature.toString()
            feelsLikeValue.text = weather.factDTO.feelsLike.toString()
            cityCoordinates.text = "lat: ${weather.infoDTO.lat} lon: ${weather.infoDTO.lon}"
        }
        mainView.showSnackBar("Работает!", "", {}, Snackbar.LENGTH_LONG)
    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.mainView, msg, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            return DetailsFragment().apply { arguments = bundle }
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }

    override fun onError(error: ResponseState) = when (error) {
        is ResponseState.ErrorOnClientSide -> {
            mainView.showSnackBar(error.errorMessage, "", {}, Snackbar.LENGTH_LONG)
        }
        is ResponseState.ErrorOnServerSide -> {
            mainView.showSnackBar(error.errorMessage, "", {}, Snackbar.LENGTH_LONG)
        }
        is ResponseState.ErrorInJSONConversion -> {
            mainView.showSnackBar(error.errorMessage, "", {}, Snackbar.LENGTH_LONG)
        }
    }
}