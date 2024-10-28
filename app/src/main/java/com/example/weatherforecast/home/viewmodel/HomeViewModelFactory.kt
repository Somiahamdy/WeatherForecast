package com.example.weatherforecast.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.model.Repo.Repo

class HomeViewModelFactory(private val repo: Repo?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
                repo?.let { HomeViewModel(it) } as T
            }else{
                throw IllegalArgumentException("ViewModel Class not Found")
            }
        }

}