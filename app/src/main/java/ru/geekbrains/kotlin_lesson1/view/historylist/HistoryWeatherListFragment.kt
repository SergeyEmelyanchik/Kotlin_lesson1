package ru.geekbrains.kotlin_lesson1.view.historylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.kotlin_lesson1.databinding.FragmentHistoryWeatherListBinding
import ru.geekbrains.kotlin_lesson1.viewmodel.AppState
import ru.geekbrains.kotlin_lesson1.viewmodel.HistoryViewModel

class HistoryWeatherListFragment : Fragment() {

    private val adapter = HistoryWeatherListAdapter()

    private var _binding: FragmentHistoryWeatherListBinding? = null
    private val binding: FragmentHistoryWeatherListBinding
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
        _binding = FragmentHistoryWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var fromHere = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        val viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        val observer = Observer<AppState> { data -> renderData(data, viewModel) }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        viewModel.getAll()
    }


    private fun initRecycler() {
        binding.listRecyclerView.also { it.adapter = adapter }
    }


    private fun renderData(data: AppState, viewModel: HistoryViewModel) = when (data) {//
        is AppState.Error -> {
            //binding.loadingLayout.apply{ visibility = View.GONE }
            //showMessage(data,viewModel)
        }
        is AppState.Loading -> {
            //binding.loadingLayout.apply{visibility = View.VISIBLE}
        }
        is AppState.Success -> {
            //binding.loadingLayout.apply{ visibility = View.GONE }
            adapter.setData(data.getWeatherListData())//weatherListData
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryWeatherListFragment()
    }
}