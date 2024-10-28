package com.example.weatherforecast.home.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.AlertActivity
import com.example.weatherforecast.FavouriteActivity
import com.example.weatherforecast.map.view.MapsActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.settings.view.SettingsActivity
import com.example.weatherforecast.db.WeatherLocalDataSource
import com.example.weatherforecast.db.WeatherLocalDataSourceImp
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.RetrofitHelper
import com.example.weatherforecast.network.WeatherRemoteDataSource
import com.example.weatherforecast.network.WeatherRemoteDataSourceImp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geoCoder: Geocoder
    private var REQUEST_LOCATION_CODE=7007
    private  var long:Double=0.0
    private  var lat:Double=0.0
    private lateinit var repo: Repo
    private lateinit var weatherCity:TextView
    private lateinit var weatherTemp:TextView
    private lateinit var weatherDes:TextView
    private lateinit var weathericon:ImageView

    private lateinit var windtxt:TextView
    private lateinit var humiditytxt:TextView
    private lateinit var pressuretxt:TextView
    private lateinit var uvtxt:TextView
    private lateinit var cloudtxt:TextView
    private lateinit var winddirtxt:TextView

    private lateinit var weatherDateTime:TextView
    private lateinit var locationimg:ImageView
    private lateinit var remoteDataSource: WeatherRemoteDataSource
    private lateinit var localDataSource: WeatherLocalDataSource
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var dailyLayoutManager:LinearLayoutManager
    private lateinit var hourlyLayoutManager: LinearLayoutManager

    private lateinit var sharedPref: SharedPreferences
    private var tempUnit:String=""
    private var windUnit:String=""
    private var language:String=""
    private var units:String=""
    private var tempUnitTxt:String=""
    private var windSpeedTxt:String=""
    private var actualLang:String=""
    private var apiKey=""



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Home"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        apiKey="0f121088b1919d00bf3ffec84d4357f9"

        //Init UI
        weatherCity=findViewById(R.id.tv_zone)
        weatherTemp=findViewById(R.id.tv_current_temp)
        weatherDes=findViewById(R.id.tv_current_des)
        locationimg=findViewById(R.id.locationimg)
        weatherDateTime=findViewById(R.id.tv_current_time_date)

        windtxt=findViewById(R.id.tv_wind)
        humiditytxt=findViewById(R.id.tv_humidity)
        pressuretxt=findViewById(R.id.tv_pressure)
        uvtxt=findViewById(R.id.tv_uv)
        cloudtxt=findViewById(R.id.tv_cloudiness)
        winddirtxt=findViewById(R.id.tv_dir)

        dailyRecyclerView=findViewById(R.id.rv_daily)
        hourlyRecyclerView=findViewById(R.id.rv_hourly)


        //view model Init
        remoteDataSource=WeatherRemoteDataSourceImp.getInstance(RetrofitHelper.service)
        localDataSource=WeatherLocalDataSourceImp.getInstance()
        repo= Repo.getInstance(remoteDataSource,localDataSource)!!
        homeViewModelFactory = HomeViewModelFactory(repo)
        homeViewModel = ViewModelProvider(this ,homeViewModelFactory ).get(HomeViewModel::class.java)

        //adapters Init
        hourlyAdapter= HourlyAdapter()
        dailyAdapter= DailyAdapter()

        //recycler view Init
        dailyLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        hourlyLayoutManager= LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        dailyRecyclerView.apply{
            adapter=dailyAdapter
            layoutManager=dailyLayoutManager
        }
        hourlyRecyclerView.apply {
            adapter=hourlyAdapter
            layoutManager=hourlyLayoutManager
        }

        //on map clicked listener
        locationimg.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_LOCATION_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION_CODE && resultCode == Activity.RESULT_OK) {

            data?.let {
                lat = it.getDoubleExtra("LATITUDE", 0.0)
                long = it.getDoubleExtra("LONGITUDE", 0.0)
                Log.d("MainActivity", "Selected Location - Lat: $lat, Long: $long")

                fetchAndUpdateData()

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_home_activity -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.nav_fav_activity -> {
                startActivity(Intent(this, FavouriteActivity::class.java))
                true
            }
            R.id.nav_alert_activity -> {
                startActivity(Intent(this, AlertActivity::class.java))
                true
            }
            R.id.nav_settings_activity -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        loadSettings()
        checkOnSettings()
        fetchAndUpdateData()
    }

    private fun loadSettings() {
        sharedPref = getSharedPreferences("SettingsSharedPref", MODE_PRIVATE)
        tempUnit = sharedPref.getString("Temp_Unit", "°C").toString()
        windUnit = sharedPref.getString("Wind_Unit", "meter").toString()
        language = sharedPref.getString("Language", "en") ?: "en"

    }

    fun checkOnSettings(){
        if(language=="Arabic"){
            actualLang="ar"
        }else{
            actualLang="en"
        }
        if(tempUnit=="°C"){
            units="metric"
            tempUnitTxt=tempUnit
            windSpeedTxt="miles/hour"
        }else if(tempUnit=="°F"){
            units="imperial"
            tempUnitTxt=tempUnit
            windSpeedTxt="meter/sec"
        }else{
            units="standard"
            windSpeedTxt="meter/sec"
            tempUnitTxt=tempUnit
        }
    }


    override fun onStart() {
        super.onStart()
        if(checkPermissions()){
            if(isLocationEnabled()){
                if(long==0.0 && lat==0.0){
                    getFreshLocation()
                }

            }else{
                enableLocationServices()
            }

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_LOCATION_CODE)
        }
    }

    fun checkPermissions(): Boolean{
        var result = false
        if((ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            || ((ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED))){
            result=true
        }
        return result
    }

    fun enableLocationServices(){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(10).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    long = location.lastLocation?.longitude!!
                    lat = location.lastLocation?.latitude!!
                    fetchAndUpdateData()
                }

            },
            Looper.myLooper()
        )

    }


    fun isLocationEnabled():Boolean{
        val loctionManager: LocationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return loctionManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || loctionManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    fun fetchAndUpdateData(){
        homeViewModel.getWeather(long,lat,apiKey,units,actualLang)
        lifecycleScope.launchWhenStarted {
            homeViewModel.weatherList.collect{ weatherResponse->
                weatherResponse?.let {
                    updateUI(weatherResponse)
                }

            }
        }
        homeViewModel.get3HourForecast(long,lat,apiKey,units,actualLang)
        lifecycleScope.launchWhenStarted{
            homeViewModel.hourlyForecastList.collect{ hourlyResponse->
                hourlyResponse?.let { hourly->
                    if (hourly.isNotEmpty()) {
                        val hourlyForecast: List<Forecast> = hourly.take(8)
                        hourlyAdapter.submitList(hourlyForecast)
                    } else {
                        Log.e("MainActivity", "Forecast list is empty")
                    }
                } ?: run {
                    Log.e("MainActivity", "Forecast response is null")
                }
            }

        }

        homeViewModel.getDailyForecast(long,lat,apiKey,units,actualLang)
        lifecycleScope.launchWhenStarted{
            homeViewModel.dailyForecastList.collect{ dailyResponse->
                dailyResponse?.let { daily->
                    if (daily.isNotEmpty()) {
                        val dailyForecast: List<Forecast> = daily.take(8)
                        dailyAdapter.submitList(dailyForecast)
                    } else {
                        Log.e("MainActivity", "Forecast list is empty")
                    }
                } ?: run {
                    Log.e("MainActivity", "Forecast response is null")
                }
            }

        }

    }

    fun updateUI(weatherResponse: WeatherResponse){
        weatherResponse?.let {
            weatherCity.text=weatherResponse.name
            weatherTemp.text= weatherResponse.main?.temp?.toInt().toString().plus(tempUnitTxt)
            weatherDes.text= weatherResponse.weather?.get(0)?.description
            weatherDateTime.text= getCurrentUTCTime(weatherResponse.timezone.toDouble())
            windtxt.text= weatherResponse.wind?.speed.toString().plus(windSpeedTxt)
            humiditytxt.text= weatherResponse.main?.humidity.toString().plus(" %")
            pressuretxt.text= weatherResponse.main?.pressure.toString().plus(" mb")
            cloudtxt.text= weatherResponse.clouds?.all.toString().plus(" km")
            winddirtxt.text= weatherResponse.wind?.gust.toString()

            Log.d("mainactivity", "onStart: city ${weatherResponse.name} ")

        }
    }

    private fun getCurrentUTCTime(shift: Double): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.add(Calendar.HOUR_OF_DAY, (shift / 3600).toInt())
        val dateFormat = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(calendar.time)
    }

}