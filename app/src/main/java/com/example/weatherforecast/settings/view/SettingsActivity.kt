package com.example.weatherforecast.settings.view

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.weatherforecast.R

class SettingsActivity : AppCompatActivity(),onSettingsClickListener {
    lateinit var tempRadioGroup:RadioGroup
    lateinit var windRadioGroup:RadioGroup
    lateinit var langRadioGroup:RadioGroup
    lateinit var sharedPref:SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.favtoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Settings" // Set the title of the page here

            setDisplayHomeAsUpEnabled(true) // Show the back button
            setDisplayShowHomeEnabled(true)
        }
        sharedPref = getSharedPreferences("SettingsSharedPref", MODE_PRIVATE)
        val editor = sharedPref.edit()

        tempRadioGroup = findViewById(R.id.tempRG)

        // Set listener for RadioGroup to detect selected option
        tempRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Find the selected RadioButton
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val selectedTempUnit = selectedRadioButton.text.toString()
            editor.putString("Temp_Unit", selectedTempUnit)
            editor.apply()
        }

        windRadioGroup = findViewById(R.id.windRG)

        // Set listener for RadioGroup to detect selected option
        windRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Find the selected RadioButton
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val selectedWindUnit = selectedRadioButton.text.toString()
            editor.putString("Wind_Unit", selectedWindUnit)
            editor.apply()
        }

        langRadioGroup = findViewById(R.id.langRG)

        // Set listener for RadioGroup to detect selected option
        langRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Find the selected RadioButton
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val selectedLanguage = selectedRadioButton.text.toString()
            editor.putString("Language", selectedLanguage)
            editor.apply()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(RESULT_OK)
        finish()
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }

    // Handle the back button click
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }

    override fun onTempUnitSelected(tempUnit: String) {
        TODO("Not yet implemented")
    }

    override fun onWindUnitSelected(windUnit: String) {
        TODO("Not yet implemented")
    }

    override fun onLanguageSelected(language: String) {
        TODO("Not yet implemented")
    }

}
