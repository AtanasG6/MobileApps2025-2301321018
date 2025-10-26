package com.example.weathertravelplanner.ui.map

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weathertravelplanner.R
import com.example.weathertravelplanner.data.remote.api.RetrofitInstance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var cityName: String = ""
    private val apiKey = "06c5fcf1013fc1260a9a1213e2630e08"

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
        fetchCityCoordinates()
    }

    private fun fetchCityCoordinates() {
        lifecycleScope.launch {
            try {
                val response = com.example.weathertravelplanner.data.remote.api.GeocodingRetrofitInstance.api.getCoordinates(cityName, 1, apiKey)

                if (response.isNotEmpty()) {
                    val location = response[0]
                    val cityCoordinates = LatLng(location.lat, location.lon)

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(cityCoordinates)
                            .title(cityName)
                    )

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityCoordinates, 12f))
                } else {
                    Toast.makeText(this@MapActivity, "City not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MapActivity, "Failed to load location", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}