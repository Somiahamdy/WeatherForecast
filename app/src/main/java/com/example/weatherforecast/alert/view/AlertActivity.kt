package com.example.weatherforecast.alert.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforecast.R
import com.example.weatherforecast.alert.viewmodel.AlertViewModel
import com.example.weatherforecast.alert.viewmodel.AlertViewModelFactory
import com.example.weatherforecast.db.AppDatabase
import com.example.weatherforecast.db.WeatherLocalDataSource
import com.example.weatherforecast.db.WeatherLocalDataSourceImp
import com.example.weatherforecast.favourites.view.FavAdapter
import com.example.weatherforecast.favourites.viewmodel.FavViewModel
import com.example.weatherforecast.favourites.viewmodel.FavViewModelFactory
import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.model.WeatherRoom
import com.example.weatherforecast.network.RetrofitHelper
import com.example.weatherforecast.network.WeatherRemoteDataSource
import com.example.weatherforecast.network.WeatherRemoteDataSourceImp
import com.example.weatherforecast.notification.WeatherNotificationReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.log

class AlertActivity : AppCompatActivity(), onAlertClickListener {
    private lateinit var alertRecycler: RecyclerView
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var addAlarm: ImageView
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var alertLayoutManager: LinearLayoutManager
    private var long:Double=0.0
    private var lat:Double=0.0
    private var tempUnit:String=""
    private var language:String=""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        val toolbar: Toolbar = findViewById(R.id.alerttoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Alerts" // Set the title of the page here

            setDisplayHomeAsUpEnabled(true) // Show the back button
            setDisplayShowHomeEnabled(true)
        }

        addAlarm = findViewById(R.id.addalarm)

        val remoteDataSource = WeatherRemoteDataSourceImp.getInstance(RetrofitHelper.service)
        val weatherDao = AppDatabase.getDatabase(applicationContext)
        val localDataSource = WeatherLocalDataSourceImp.getInstance(this)
        val repo = Repo.getInstance(remoteDataSource, localDataSource)!!
        val alertViewModelFactory = AlertViewModelFactory(repo)
        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)

        alertAdapter = AlertAdapter(this)
        alertLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        alertRecycler = findViewById(R.id.alertrv)
        alertRecycler.apply {
            adapter = alertAdapter
            layoutManager = alertLayoutManager
        }

        var alertSharedPre:SharedPreferences=getSharedPreferences("alertsSharedPref", MODE_PRIVATE)
         long= alertSharedPre.getLong("long",0)?.toDouble()!!
         lat= alertSharedPre.getLong("lat",0)?.toDouble()!!
        Log.d("alert activity", "alert sh pref : long $long")

        var settingSharedPref:SharedPreferences = getSharedPreferences("SettingsSharedPref", MODE_PRIVATE)
        tempUnit = settingSharedPref.getString("Temp_Unit", "°C").toString()
        language  = settingSharedPref.getString("Language", "en") ?: "en"


        addAlarm.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)

                TimePickerDialog(this, { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    scheduleWeatherAlarm(this, calendar.timeInMillis)

                    // Create an AlarmRoom object and save it to the database
                    val alarm = AlarmRoom(
                        timeInMillis = calendar.timeInMillis,
                        formattedTime = calendar.time.toString() // Format as needed
                    )
                    alertViewModel.addAlarm(alarm)

                    Toast.makeText(this, "Alarm set for: ${calendar.time}", Toast.LENGTH_SHORT).show()
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001 // Any unique request code
                )
            }
        }

        createNotificationChannel()

        lifecycleScope.launchWhenStarted {
            alertViewModel.alarmList.collectLatest { alarmList ->
                alertAdapter.submitList(alarmList)
            }
        }

        // Load alarms when the activity starts
        alertViewModel.getAllAlarm()
    }
    override fun onSupportNavigateUp(): Boolean {
        setResult(RESULT_OK)
        finish()
        return true
    }


    private fun scheduleWeatherAlarm(context: Context, timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WeatherNotificationReceiver::class.java)
        intent.putExtra("long",long)
        intent.putExtra("lat",lat)
        intent.putExtra("unit",tempUnit)
        intent.putExtra("lang",language)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Weather Channel"
            val descriptionText = "Notifications for weather alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("weather_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onRemoveClickListner(alarmRoom: AlarmRoom) {
        alertViewModel.deleteAlarm(alarmRoom)
    }
}