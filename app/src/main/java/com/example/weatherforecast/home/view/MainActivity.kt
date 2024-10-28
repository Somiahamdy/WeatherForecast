package com.example.weatherforecast.home.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.AlertActivity
import com.example.weatherforecast.FavouriteActivity
import com.example.weatherforecast.MapsActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.SettingsActivity
import com.example.weatherforecast.db.WeatherLocalDataSource
import com.example.weatherforecast.db.WeatherLocalDataSourceImp
import com.example.weatherforecast.home.viewmodel.HomeViewModel
import com.example.weatherforecast.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.network.RetrofitHelper
import com.example.weatherforecast.network.WeatherRemoteDataSource
import com.example.weatherforecast.network.WeatherRemoteDataSourceImp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import retrofit2.Response
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



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Home"
        weatherCity=findViewById(R.id.tv_zone)
        weatherTemp=findViewById(R.id.tv_current_temp)
        //weathericon=findViewById(R.id.iv_current)
        weatherDes=findViewById(R.id.tv_current_des)
        locationimg=findViewById(R.id.locationimg)
        weatherDateTime=findViewById(R.id.tv_current_time_date)

        windtxt=findViewById(R.id.tv_wind)
        humiditytxt=findViewById(R.id.tv_humidity)
        pressuretxt=findViewById(R.id.tv_pressure)
        uvtxt=findViewById(R.id.tv_uv)
        cloudtxt=findViewById(R.id.tv_cloudiness)
        winddirtxt=findViewById(R.id.tv_dir)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        remoteDataSource=WeatherRemoteDataSourceImp.getInstance(RetrofitHelper.service)
        localDataSource=WeatherLocalDataSourceImp.getInstance()
        repo= Repo.getInstance(remoteDataSource,localDataSource)!!
        homeViewModelFactory = HomeViewModelFactory(repo)
        homeViewModel = ViewModelProvider(this ,homeViewModelFactory ).get(HomeViewModel::class.java)
        //homeViewModel.getForecast(long,lat,"en","375d11598481406538e244d548560243")
        dailyRecyclerView=findViewById(R.id.rv_daily)
        hourlyRecyclerView=findViewById(R.id.rv_hourly)
        hourlyAdapter= HourlyAdapter()
        dailyAdapter= DailyAdapter()
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

        // MainActivity.kt
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
                homeViewModel.getWeather(long,lat,"0f121088b1919d00bf3ffec84d4357f9","metric")
                lifecycleScope.launchWhenStarted {
                    homeViewModel.weatherList.collect{ weatherResponse->
                        weatherResponse?.let {
                            weatherCity.text=weatherResponse.name
                            weatherTemp.text= weatherResponse.main?.temp?.toInt().toString().plus("°C")
                            //weathericon.setImageResource(getIconRes(weatherResponse.weather?.get(0)?.icon))
                            weatherDes.text= weatherResponse.weather?.get(0)?.description
                            weatherDateTime.text= getCurrentUTCTime(weatherResponse.timezone.toDouble())
                            windtxt.text= weatherResponse.wind?.speed.toString().plus(" km/h")
                            humiditytxt.text= weatherResponse.main?.humidity.toString().plus(" %")
                            pressuretxt.text= weatherResponse.main?.pressure.toString().plus(" mb")
                            cloudtxt.text= weatherResponse.clouds?.all.toString().plus(" km")
                            winddirtxt.text= weatherResponse.wind?.gust.toString()

                            Log.d("mainactivity", "onStart: city ${weatherResponse.name} ")

                        }

                    }
                }
                homeViewModel.get3HourForecast(long,lat,"0f121088b1919d00bf3ffec84d4357f9","metric")
                lifecycleScope.launchWhenStarted{
                    homeViewModel.hourlyForecastList.collect{ hourlyResponse->
                        hourlyResponse?.let { hourly->
                            if (hourly.isNotEmpty()) {
                                // Get the first 8 entries if they exist
                                val hourlyForecast: List<Forecast> = hourly.take(8)
                                hourlyAdapter.submitList(hourlyForecast)
                            } else {
                                // Handle the case where the list is empty
                                Log.e("MainActivity", "Forecast list is empty")
                            }
                        } ?: run {
                            // Handle the case where forecastResponse is null
                            Log.e("MainActivity", "Forecast response is null")
                        }
                    }

                }

                homeViewModel.getDailyForecast(long,lat,"0f121088b1919d00bf3ffec84d4357f9","metric")
                lifecycleScope.launchWhenStarted{
                    homeViewModel.dailyForecastList.collect{ dailyResponse->
                        dailyResponse?.let { daily->
                            if (daily.isNotEmpty()) {
                                // Get the first 8 entries if they exist
                                val dailyForecast: List<Forecast> = daily.take(8)
                                dailyAdapter.submitList(dailyForecast)
                            } else {
                                // Handle the case where the list is empty
                                Log.e("MainActivity", "Forecast list is empty")
                            }
                        } ?: run {
                            // Handle the case where forecastResponse is null
                            Log.e("MainActivity", "Forecast response is null")
                        }
                    }

                }

                // Now you can use these coordinates to fetch weather data or update UI
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

    override fun onStart() {
        super.onStart()
        if(checkPermissions()){
            if(isLocationEnabled()){
                if(long==0.0 && lat==0.0){
                    getFreshLocation()
                }
                //homeViewModel.getWeather(long,lat,"0f121088b1919d00bf3ffec84d4357f9")


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
                    //homeViewModel.getForecast(long, lat, "en","375d11598481406538e244d548560243")
                    homeViewModel.getWeather(long,lat,"0f121088b1919d00bf3ffec84d4357f9","metric")
                    lifecycleScope.launchWhenStarted {
                        homeViewModel.weatherList.collect{ weatherResponse->
                            weatherResponse?.let {
                                weatherCity.text=weatherResponse.name
                                weatherTemp.text= weatherResponse.main?.temp?.toInt().toString().plus("°C")
                                //weathericon.setImageResource(getIconRes(weatherResponse.weather?.get(0)?.icon))
                                weatherDes.text= weatherResponse.weather?.get(0)?.description
                                weatherDateTime.text= getCurrentUTCTime(weatherResponse.timezone.toDouble())
                                windtxt.text= weatherResponse.wind?.speed.toString().plus(" km/h")
                                humiditytxt.text= weatherResponse.main?.humidity.toString().plus(" %")
                                pressuretxt.text= weatherResponse.main?.pressure.toString().plus(" mb")
                                cloudtxt.text= weatherResponse.clouds?.all.toString().plus(" km")
                                winddirtxt.text= weatherResponse.wind?.gust.toString()

                                Log.d("mainactivity", "onStart: city ${weatherResponse.name} ")

                            }

                        }
                    }
                    homeViewModel.get3HourForecast(long,lat,"0f121088b1919d00bf3ffec84d4357f9","metric")
                    lifecycleScope.launchWhenStarted{
                        homeViewModel.hourlyForecastList.collect{ hourlyResponse->
                            hourlyResponse?.let { hourly->
                                if (hourly.isNotEmpty()) {
                                    // Get the first 8 entries if they exist
                                    val hourlyForecast: List<Forecast> = hourly.take(8)
                                    hourlyAdapter.submitList(hourlyForecast)
                                } else {
                                    // Handle the case where the list is empty
                                    Log.e("MainActivity", "Forecast list is empty")
                                }
                            } ?: run {
                                    // Handle the case where forecastResponse is null
                                    Log.e("MainActivity", "Forecast response is null")
                                }
                            }

                        }

                    homeViewModel.getDailyForecast(long,lat,"0f121088b1919d00bf3ffec84d4357f9","metric")
                    lifecycleScope.launchWhenStarted{
                        homeViewModel.dailyForecastList.collect{ dailyResponse->
                            dailyResponse?.let { daily->
                                if (daily.isNotEmpty()) {
                                    // Get the first 8 entries if they exist
                                    val dailyForecast: List<Forecast> = daily.take(8)
                                    dailyAdapter.submitList(dailyForecast)
                                } else {
                                    // Handle the case where the list is empty
                                    Log.e("MainActivity", "Forecast list is empty")
                                }
                            } ?: run {
                                // Handle the case where forecastResponse is null
                                Log.e("MainActivity", "Forecast response is null")
                            }
                        }

                    }

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
    private fun getCurrentUTCTime(shift: Double): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.add(Calendar.HOUR_OF_DAY, (shift / 3600).toInt())
        val dateFormat = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(calendar.time)
    }

}