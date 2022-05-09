package ru.geekbrains.kotlin_lesson1.view.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import ru.geekbrains.kotlin_lesson1.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin_lesson1.repository.*
import ru.geekbrains.kotlin_lesson1.utlis.*
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsState
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsViewModel


class DetailsFragment : Fragment(){
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
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner,object :Observer<DetailsState>{
            override fun onChanged(t: DetailsState) {
                renderData(t)
            }
        })

        requireArguments().getParcelable<Weather>(BUNDLE_WEATHER_KEY)?.let {
            viewModel.getWeather(it.city)
        }
    }
    private fun renderData(detailsState: DetailsState) {

        when(detailsState){
            is DetailsState.Error -> TODO()
            DetailsState.Loading -> TODO()
            is DetailsState.Success -> {
                val weather = detailsState.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.cityName
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "lat: ${weather.city.lat} lon: ${weather.city.lon}"
                }
                mainView.showSnackBar("Работает!", "", {}, Snackbar.LENGTH_LONG)
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            return DetailsFragment().apply { arguments = bundle }
        }
    }
}