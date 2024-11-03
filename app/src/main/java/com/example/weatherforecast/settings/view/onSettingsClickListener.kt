package com.example.weatherforecast.settings.view

interface onSettingsClickListener {
    fun onTempUnitSelected(tempUnit:String)
    fun onWindUnitSelected(windUnit:String)
    fun onLanguageSelected(language: String)


}