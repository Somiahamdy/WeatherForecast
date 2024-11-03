package com.example.weatherforecast.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.model.Repo.Repo

class FavViewModelFactory (private val repo: Repo?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavViewModel::class.java)){
            repo?.let { FavViewModel(it) } as T
        }else{
            throw IllegalArgumentException("ViewModel Class not Found")
        }
    }

}