package com.example.weatherforecast.home.view

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Forecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//import com.example.weatherforecast.model.Daily


class DailyAdapter: ListAdapter<Forecast,DailyAdapter.ViewHolder>(DailyDiffUtil()) {
    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val dayName:TextView=item.findViewById(R.id.tv_day)
        val dweatherimg:ImageView=item.findViewById(R.id.iv_weather)
        val dweatherdes:TextView=item.findViewById(R.id.tv_des)
        val dweathertemp:TextView=item.findViewById(R.id.tv_temp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.daily_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentDayWeather=getItem(position)
        var date= Date(currentDayWeather.dt.toLong()*1000)
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        var unit:String=""

        val formattedDay = dayFormat.format(date)
        if(position==0){
            holder.dayName.text="Today"
        }else{
            holder.dayName.text=formattedDay
        }
        holder.dweatherimg.setImageResource(getIconRes(currentDayWeather.weather?.get(0)?.icon))
        holder.dweatherdes.text= currentDayWeather.weather?.get(0)?.description
        holder.dweathertemp.text= currentDayWeather.main?.temp_max?.toInt().toString().plus("°C").plus(" / ")
            .plus(currentDayWeather.main?.temp_min?.toInt().toString()).plus("°C")
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

