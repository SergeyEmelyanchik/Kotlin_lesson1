package ru.geekbrains.kotlin_lesson1.view.details


import android.graphics.Insets.add
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import ru.geekbrains.kotlin_lesson1.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin_lesson1.repository.*
import ru.geekbrains.kotlin_lesson1.utlis.*
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsState
import ru.geekbrains.kotlin_lesson1.viewmodel.DetailsViewModel


class DetailsFragment : Fragment() {
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
        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<DetailsState> {
            override fun onChanged(t: DetailsState) {
                renderData(t)
            }
        })

        requireArguments().getParcelable<Weather>(BUNDLE_WEATHER_KEY)?.let {
            viewModel.getWeather(it.city)
        }
    }

    private fun renderData(detailsState: DetailsState) {

        when (detailsState) {
            is DetailsState.Error -> {
                loadingLayout.visibility = View.GONE
                mainView.showSnackBar(detailsState.error.toString(), "", {}, Snackbar.LENGTH_LONG)
            }
            DetailsState.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }
            is DetailsState.Success -> {
                loadingLayout.visibility = View.GONE
                val weather = detailsState.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.cityName
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "lat: ${weather.city.lat} lon: ${weather.city.lon}"
                    Glide.with(requireContext())
                        .load("https://i.ya-webdesign.com/images/cityscape-at-night-png-2.png")
                        .into(headerCityIcon)
                    Picasso.get()
                        ?.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    icon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")

                }
                mainView.showSnackBar("Работает!", "", {}, Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            return DetailsFragment().apply { arguments = bundle }
        }
    }
}