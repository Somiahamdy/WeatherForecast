package com.example.weatherforecast.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.home.view.HourlyDiffUtil
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Forecast
//import com.example.weatherforecast.model.Hourly
import kotlinx.coroutines.processNextEventInCurrentThread
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class HourlyAdapter: ListAdapter<Forecast, HourlyAdapter.ViewHolder>(HourlyDiffUtil()) {
    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val htime:TextView=item.findViewById(R.id.tv_time)
        val hweatherimg:ImageView=item.findViewById(R.id.iv_weather)
        val htemp:TextView=item.findViewById(R.id.tv_temp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.hourly_layout, parent, false)
        return ViewHolder(view)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentForecast=getItem(position)
        holder.htime.text= unixTimeToReadableDate(currentForecast.dt.toLong())
        holder.hweatherimg.setImageResource(getIconRes(currentForecast.weather?.get(0)?.icon))
        holder.htemp.text= currentForecast.main?.temp?.toInt().toString().plus("°C")

    }
    private fun unixTimeToReadableDate(unixTime: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault() // Set your desired time zone
        val date = Date(unixTime * 1000)
        return dateFormat.format(date)
    }
    fun getIconRes(icon: String?) = when (icon) {
        "01n" -> R.drawable.ic_01n
        "01d" -> R.drawable.ic_01d
        "02n" -> R.drawable.ic_02n
        "02d" -> R.drawable.ic_02d
        "03n", "03d" -> R.drawable.ic_03
        "04n" -> R.drawable.ic_04n
        "04d" -> R.drawable.ic_04d
        "09n", "09d" -> R.drawable.ic_09
        "10n" -> R.drawable.ic_10n
        "10d" -> R.drawable.ic_10d
        "11n", "11d" -> R.drawable.ic_11
        "13n" -> R.drawable.ic_13n
        "13d" -> R.drawable.ic_13d
        "50n", "50d" -> R.drawable.ic_50
        else -> R.drawable.ic_04d
    }

}
