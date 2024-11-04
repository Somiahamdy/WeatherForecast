package com.example.weatherforecast.favourites.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.home.view.DailyAdapter
import com.example.weatherforecast.home.view.DailyAdapter.ViewHolder
import com.example.weatherforecast.home.view.DailyDiffUtil
import com.example.weatherforecast.home.view.MainActivity
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.WeatherRoom

class FavAdapter(val listner:onFavClickListener) :  ListAdapter<WeatherRoom, FavAdapter.ViewHolder>(FavDiffUtil()) {
    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val name: TextView =item.findViewById(R.id.tv_zone)
        val dweatherimg: ImageView =item.findViewById(R.id.iv_icon)
        val dweatherdes: TextView =item.findViewById(R.id.tv_des)
        val remove:ImageView=item.findViewById(R.id.removebtn)
        val weatherCard:CardView=item.findViewById(R.id.favcv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.fav_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentWeather = getItem(position)
        holder.dweatherimg.setImageResource(getIconRes(currentWeather.icon))
        holder.dweatherdes.text= currentWeather.description
        holder.name.text=currentWeather.name
        holder.remove.setOnClickListener {
            listner.onRemoveClickListener(currentWeather)
        }
        holder.weatherCard.setOnClickListener {
            listner.onFavClickListener(currentWeather)
        }
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