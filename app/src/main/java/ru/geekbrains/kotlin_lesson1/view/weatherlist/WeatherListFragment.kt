package ru.geekbrains.kotlin_lesson1.view.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin_lesson1.R
import ru.geekbrains.kotlin_lesson1.databinding.FragmentWeatherListBinding
import ru.geekbrains.kotlin_lesson1.repository.Weather
import ru.geekbrains.kotlin_lesson1.utlis.KEY_BUNDLE_WEATHER
import ru.geekbrains.kotlin_lesson1.view.details.DetailsFragment
import ru.geekbrains.kotlin_lesson1.viewmodel.AppState
import ru.geekbrains.kotlin_lesson1.viewmodel.MainViewModel

class WeatherListFragment : Fragment(), OnItemListClickListener {


    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    val adapter = WeatherListAdapter(this)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    var isBelarus = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.btnOne.setOnClickListener {  }
        // view.findViewById<TextView>(R.id.btnOne).setOnClickListener {  }
        // view.findViewById<Button>(R.id.btnOne).setOnClickListener {  }
        binding.recyclerView.adapter = adapter // TODO HW вынесты в initRecycler()
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //val observer = Observer<Any>{ renderData(it) }
        val observer =
            Observer<AppState> { data -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        binding.floatingActionButton.setOnClickListener {
            isBelarus = !isBelarus
            if (isBelarus) {
                viewModel.getWeatherRussia()
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_belarus
                    )
                )
            } else {
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_earth
                    )
                )
                viewModel.getWeatherWorld()
            }
        }
        viewModel.getWeatherRussia()
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_LONG)
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherList)


                /* binding.cityName.text = data.weatherData.city.name.toString()
                 binding.temperatureValue.text = data.weatherData.temperature.toString()
                 binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
                 binding.cityCoordinates.text = "${data.weatherData.city.lat} ${data.weatherData.city.lon}"
                 Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()*/
                //Toast.makeText(requireContext(),"РАБОТАЕТ",Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_WEATHER, weather)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(bundle)
        ).addToBackStack("").commit()
    }
}