package com.example.weatherforecast.map.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


import android.app.Activity
import android.content.Intent
import com.example.weatherforecast.R
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow


class MapsActivity : AppCompatActivity() {

    private lateinit var osmMapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Initialize the OsmDroid configuration
        Configuration.getInstance().userAgentValue = packageName

        // Set up the MapView
        osmMapView = findViewById(R.id.osmMapView)
        osmMapView.setTileSource(TileSourceFactory.MAPNIK)
        osmMapView.setMultiTouchControls(true)

        // Set initial map zoom and location
        val mapController: IMapController = osmMapView.controller
        mapController.setZoom(5.0)
        mapController.setCenter(GeoPoint(20.0, 78.0)) // Center on a default location (e.g., central India)

        // Add MapEventsOverlay to handle tap events
        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                // Clear existing markers
                osmMapView.overlays.clear()

                // Add a new marker at the selected location
                val marker = Marker(osmMapView).apply {
                    position = p
                    title = "Selected Location"
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    infoWindow = BasicInfoWindow(R.layout.custom_info_window, osmMapView)
                }
                osmMapView.overlays.add(marker)
                osmMapView.invalidate()

                // Send the selected location back to MainActivity
                val resultIntent = Intent().apply {
                    putExtra("LATITUDE", p.latitude)
                    putExtra("LONGITUDE", p.longitude)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close MapActivity
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }

        // Add the overlay to the map
        val eventsOverlay = MapEventsOverlay(mapEventsReceiver)
        osmMapView.overlays.add(eventsOverlay)
    }

    override fun onResume() {
        super.onResume()
        osmMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        osmMapView.onPause()
    }
}