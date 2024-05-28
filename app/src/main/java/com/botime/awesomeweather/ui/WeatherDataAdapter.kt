package com.botime.awesomeweather.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.botime.awesomeweather.R
import com.botime.awesomeweather.data.FiveDayForecastResponse
import com.bumptech.glide.Glide

/**
 * Adapter for the RecyclerView to display weather data.
 */
class WeatherDataAdapter(private var weatherData: FiveDayForecastResponse) :
    RecyclerView.Adapter<WeatherDataAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weatherIcon: ImageView
        val weatherDescription: TextView
        val dateTime: TextView
        val temperature: TextView

        init {
            weatherIcon = view.findViewById(R.id.weatherIcon)
            weatherDescription = view.findViewById(R.id.weatherDescription)
            dateTime = view.findViewById(R.id.dateTime)
            temperature = view.findViewById(R.id.temperature)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(weatherData: FiveDayForecastResponse) {
        this.weatherData = weatherData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherData.list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTime.text = weatherData.list[position].datetimeText
        holder.weatherDescription.text = weatherData.list[position].weather[0].description
        holder.temperature.text = weatherData.list[position].main.temperature.toString()
        val weatherIconUrl = "https://openweathermap.org/img/wn/${weatherData.list[position].weather[0].icon}@2x.png"
        Glide.with(holder.itemView.context)
            .load(weatherIconUrl)
            .into(holder.weatherIcon)
    }
}