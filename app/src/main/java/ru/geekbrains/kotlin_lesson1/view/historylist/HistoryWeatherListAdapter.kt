package ru.geekbrains.kotlin_lesson1.view.historylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.geekbrains.kotlin_lesson1.databinding.FragmentHistoryWeatherListRecyclerItemBinding
import ru.geekbrains.kotlin_lesson1.repository.Weather

class HistoryWeatherListAdapter(
    private var data: List<Weather> = listOf()
) :
    RecyclerView.Adapter<HistoryWeatherListAdapter.CityHolder>() {

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentHistoryWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data.get(position))
        //holder.bind(data.get(position))
    }

    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            val binding = FragmentHistoryWeatherListRecyclerItemBinding.bind(itemView)
//            binding.tvCityName.text = weather.city.cityName
//            binding.root.setOnClickListener {
//            onItemListClickListener.onItemClick(weather)
            with(binding) {
                tvCityName.text = weather.city.cityName
                tvTemperature.text = weather.temperature.toString()
                tvFeelsLike.text = weather.feelsLike.toString()
                ivIcon.load(weather.icon)
            }
        }
    }
}