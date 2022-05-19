package ru.geekbrains.kotlin_lesson1.view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
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
import ru.geekbrains.kotlin_lesson1.repository.City
import ru.geekbrains.kotlin_lesson1.repository.Weather
import ru.geekbrains.kotlin_lesson1.utlis.BUNDLE_WEATHER_KEY
import ru.geekbrains.kotlin_lesson1.utlis.DEFAULT_VALUE_BOOLEAN_FALSE
import ru.geekbrains.kotlin_lesson1.utlis.PREFERENCE_KEY_FILE_NAME_SETTINGS
import ru.geekbrains.kotlin_lesson1.utlis.PREFERENCE_KEY_FILE_NAME_SETTINGS_IS_RUSSIAN
import ru.geekbrains.kotlin_lesson1.view.details.DetailsFragment
import ru.geekbrains.kotlin_lesson1.viewmodel.AppState
import ru.geekbrains.kotlin_lesson1.viewmodel.MainViewModel
import java.util.*

class WeatherListFragment : Fragment(), OnItemListClickListener {
    private var isRussia = DEFAULT_VALUE_BOOLEAN_FALSE
    private val adapter = WeatherListAdapter(this)
    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
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
        isRussia = requireActivity().getSharedPreferences(
            PREFERENCE_KEY_FILE_NAME_SETTINGS,
            Context.MODE_PRIVATE
        )
            .getBoolean(PREFERENCE_KEY_FILE_NAME_SETTINGS_IS_RUSSIAN, DEFAULT_VALUE_BOOLEAN_FALSE)
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { data -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        setupFabCities(viewModel, false)
        binding.floatingActionButton.setOnClickListener {
            setupFabCities(viewModel, true)
        }
        viewModel.getWeatherRussia()
        setupFabLocation()
    }

    private fun initRecycler() {
        binding.listRecyclerView.also { it.adapter = adapter }
    }

    private fun setupFabLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            explain()
        } else {
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.dialog_rationale_message))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(resources.getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    val REQUEST_CODE = 888
    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun getAddressByLocation(location: Location) {
        //val geocoder = Geocoder(requireContext())
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val timeStump = System.currentTimeMillis()
        Thread {
            val addressText =
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    10000
                )[0].getAddressLine(0)
            requireActivity().runOnUiThread{
                showAddressDialog(addressText, location)
            }
        }.start()
        Log.d("@@@", " прошло ${System.currentTimeMillis() + timeStump}")
    }

    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListener
                    )
                }

            }
        }
    }
    private fun setupFabCities(viewModel: MainViewModel, redraw: Boolean) {
        if (redraw) {
            isRussia = !isRussia
        }
        if (isRussia) {
            viewModel.getWeatherRussia()
            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_belarus
                )
            )
        } else {
            viewModel.getWeatherWorld()
            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_earth
                )
            )
        }
    }

    private fun renderData(data: AppState, viewModel: MainViewModel) = when (data) {//
        is AppState.Error -> {
            binding.loadingLayout.apply { visibility = View.GONE }
            showMessage(data, viewModel)
        }
        is AppState.Loading -> {
            binding.loadingLayout.apply { visibility = View.VISIBLE }
        }
        is AppState.Success -> {
            binding.loadingLayout.apply { visibility = View.GONE }
            adapter.setData(data.getWeatherListData())//weatherListData
        }
    }

    private fun showMessage(msg: AppState, viewModel: MainViewModel) {
        val mySnack: Snackbar = Snackbar.make(
            binding.root,
            "Что-то не загрузилось \n${msg.toString()}",
            Snackbar.LENGTH_LONG
        )
        mySnack.setAction("Попробовать еще?", View.OnClickListener {
            setupFabCities(viewModel, false)
        })
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(bundle.apply { putParcelable(BUNDLE_WEATHER_KEY, weather) })
        ).addToBackStack("").commit()
    }
    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


}

