package com.example.weatherforecast.favourites.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.db.AppDatabase
import com.example.weatherforecast.db.WeatherLocalDataSource
import com.example.weatherforecast.db.WeatherLocalDataSourceImp
import com.example.weatherforecast.favourites.viewmodel.FavViewModel
import com.example.weatherforecast.favourites.viewmodel.FavViewModelFactory
import com.example.weatherforecast.home.view.MainActivity
import com.example.weatherforecast.map.view.MapsActivity
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.model.WeatherRoom
import com.example.weatherforecast.network.RetrofitHelper
import com.example.weatherforecast.network.WeatherRemoteDataSource
import com.example.weatherforecast.network.WeatherRemoteDataSourceImp

class FavouriteActivity : AppCompatActivity(), onFavClickListener {
    private var REQUEST_LOCATION_CODE=7007
    private lateinit var addToFav: ImageView
    private lateinit var remoteDataSource: WeatherRemoteDataSource
    private lateinit var localDataSource: WeatherLocalDataSource
    private lateinit var favViewModelFactory: FavViewModelFactory
    private lateinit var favViewModel: FavViewModel
    private lateinit var repo: Repo
    private lateinit var favAdapter:FavAdapter
    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favLayoutManager: LinearLayoutManager
    private lateinit var weatherRoom:WeatherRoom
    private var lat:Double=0.0
    private var lon:Double=0.0
    private var name: String=""
    private var temp_min: Double=0.0
    private var temp_max: Double=0.0
    private var description: String=""
    private var icon: String=""
    private var id: Int=0
    private var apiKey="0f121088b1919d00bf3ffec84d4357f9"



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favourite)
        val toolbar: Toolbar = findViewById(R.id.favtoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Favourites" // Set the title of the page here

            setDisplayHomeAsUpEnabled(true) // Show the back button
            setDisplayShowHomeEnabled(true)
        }

        addToFav=findViewById(R.id.addfavbtn)
        favRecyclerView=findViewById(R.id.favrv)

        //weatherRoom=WeatherRoom(lon,lat,name,temp_min,temp_max,description,icon)

        //view model Init
        remoteDataSource= WeatherRemoteDataSourceImp.getInstance(RetrofitHelper.service)
        val weatherDao = AppDatabase.getDatabase(applicationContext)
        localDataSource= WeatherLocalDataSourceImp.getInstance(this)
        repo= Repo.getInstance(remoteDataSource,localDataSource)!!
        favViewModelFactory = FavViewModelFactory(repo)
        favViewModel = ViewModelProvider(this ,favViewModelFactory ).get(FavViewModel::class.java)

        favAdapter= FavAdapter(this)
        favLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)



        favRecyclerView.apply{
            adapter=favAdapter
            layoutManager=favLayoutManager
        }

        // Fetch and observe the favorites list from Room
        favViewModel.getFavWeather()
        lifecycleScope.launchWhenStarted {
            favViewModel.favList.collect { favList ->
                favAdapter.submitList(favList) // Set data to the adapter
            }
        }

        addToFav.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_LOCATION_CODE)
        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION_CODE && resultCode == Activity.RESULT_OK) {

            data?.let {
                lat = it.getDoubleExtra("LATITUDE", 0.0)
                lon = it.getDoubleExtra("LONGITUDE", 0.0)
                Log.d("TAG", "Received coordinates - lat: $lat, lon: $lon")
                favViewModel.getWeather(lon,lat,apiKey,"metric","en")
                lifecycleScope.launchWhenStarted {
                    favViewModel.weatherList.collect{ weatherResponse->
                        weatherResponse?.let {
                            name= it.name.toString()
                            temp_max= it.main?.temp_max!!
                            temp_min= it.main?.temp_min!!
                            description= it.weather?.get(0)?.description.toString()
                            id=it.id
                            icon= it.weather?.get(0)?.icon.toString()
                            // Only create and insert when all data is ready
                            weatherRoom = WeatherRoom(lon, lat, name, temp_min, temp_max, description, icon)
                            Log.d("favactivity", "tempmin: $temp_min ")
                            favViewModel.insertWeatherToFav(weatherRoom)
                            lifecycleScope.launchWhenStarted {
                                favViewModel.favList.collect { favList ->
                                    favAdapter.submitList(favList) // Set data to the adapter
                                }
                            }

                            // Refresh favorites list after insertion
                            //favViewModel.getFavWeather()
                        }

                    }
                }

                    }


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

    override fun onRemoveClickListener(weatherRoom: WeatherRoom) {
        favViewModel.deleteWeatherFromFav(weatherRoom)
        lifecycleScope.launchWhenStarted {
            favViewModel.favList.collect { favList ->
                favAdapter.submitList(favList) // Set data to the adapter
            }
        }

    }

    override fun onFavClickListener(weatherRoom: WeatherRoom) {
        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra("long", weatherRoom.lon)
        intent.putExtra("lat", weatherRoom.lat)
        startActivity(intent)
    }

}