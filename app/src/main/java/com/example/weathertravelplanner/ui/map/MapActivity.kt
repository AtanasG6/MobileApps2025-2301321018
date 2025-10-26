package com.example.weathertravelplanner.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weathertravelplanner.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var cityName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        cityName = intent.getStringExtra("CITY_NAME") ?: ""
        title = "Map - $cityName"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // For simplicity, we'll use approximate coordinates for common cities
        // In a real app, you would use Geocoding API to get exact coordinates
        val cityCoordinates = getCityCoordinates(cityName)

        googleMap.addMarker(
            MarkerOptions()
                .position(cityCoordinates)
                .title(cityName)
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityCoordinates, 12f))
    }

    private fun getCityCoordinates(city: String): LatLng {
        // Simple hardcoded coordinates for demo purposes
        return when (city.lowercase()) {
            "sofia" -> LatLng(42.6977, 23.3219)
            "plovdiv" -> LatLng(42.1354, 24.7453)
            "varna" -> LatLng(43.2141, 27.9147)
            "burgas" -> LatLng(42.5048, 27.4626)
            "paris" -> LatLng(48.8566, 2.3522)
            "london" -> LatLng(51.5074, -0.1278)
            "new york" -> LatLng(40.7128, -74.0060)
            "tokyo" -> LatLng(35.6762, 139.6503)
            else -> LatLng(42.6977, 23.3219) // Default to Sofia
        }
    }
}