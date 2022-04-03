package ru.geekbrains.kotlin_lesson1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.kotlin_lesson1.databinding.FragmentMainBinding
import ru.geekbrains.kotlin_lesson1.viewmodel.MainViewModel


class MainFragment : Fragment() {


    lateinit var binding: FragmentMainBinding// утечка памяти

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOne.setOnClickListener { }
        // view.findViewById<Button>(R.id.btnOne).setOnClickListener {  }то же самое, что и выше но не ловит nuul point reception
        // view.findViewById<TextView>(R.id.btnOne).setOnClickListener {  }

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //val observer = Observer<Any>{ renderData(it) } пока обойдемся без лямды
        val observer = object : Observer<Any> {
            override fun onChanged(data: Any) {
                renderData(data)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    private fun renderData(data: Any) {
        Toast.makeText(requireContext(), "ВСЁ РАБОТАЕТ", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}