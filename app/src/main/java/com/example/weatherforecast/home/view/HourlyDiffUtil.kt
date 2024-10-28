package com.example.weatherforecast.home.view

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecast.model.Forecast


class HourlyDiffUtil : DiffUtil.ItemCallback<Forecast>() {

        override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
            return oldItem.sys?.id  == newItem.sys?.id
        }

    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem==newItem
    }
}
