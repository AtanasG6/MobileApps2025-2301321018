package com.example.weathertravelplanner.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weathertravelplanner.R
import com.example.weathertravelplanner.data.local.entity.Trip
import com.example.weathertravelplanner.data.remote.api.WeatherRetrofitInstance
import com.example.weathertravelplanner.ui.trips.AddEditTripActivity
import com.example.weathertravelplanner.ui.trips.TripViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: TripViewModel
    private val apiKey = "06c5fcf1013fc1260a9a1213e2630e08"
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var currentTripId: Long = 0
    private var currentCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        currentTripId = intent.getLongExtra("TRIP_ID", 0)

        val tvTripName = findViewById<TextView>(R.id.tvDetailTripName)
        val tvCity = findViewById<TextView>(R.id.tvDetailCity)
        val tvDates = findViewById<TextView>(R.id.tvDetailDates)
        val tvNotes = findViewById<TextView>(R.id.tvDetailNotes)
        val tvWeatherInfo = findViewById<TextView>(R.id.tvWeatherInfo)
        val progressBar = findViewById<ProgressBar>(R.id.weatherProgressBar)
        val ivWeatherIcon = findViewById<ImageView>(R.id.ivWeatherIcon)

        viewModel.getTripById(currentTripId).observe(this) { trip ->
            trip?.let {
                tvTripName.text = it.name
                tvCity.text = it.city
                currentCity = it.city
                tvDates.text = "${dateFormat.format(Date(it.startDate))} - ${dateFormat.format(Date(it.endDate))}"
                tvNotes.text = if (it.notes.isNotEmpty()) it.notes else "No notes"

                // Fetch weather
                fetchWeather(it.city, tvWeatherInfo, progressBar, ivWeatherIcon)
            }
        }

        findViewById<Button>(R.id.btnEditTrip).setOnClickListener {
            val intent = Intent(this, AddEditTripActivity::class.java)
            intent.putExtra("TRIP_ID", currentTripId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnDeleteTripFromDetails).setOnClickListener {
            deleteTrip()
        }

        findViewById<Button>(R.id.btnViewOnMap).setOnClickListener {
            if (currentCity.isNotEmpty()) {
                val intent = Intent(this, com.example.weathertravelplanner.ui.map.MapActivity::class.java)
                intent.putExtra("CITY_NAME", currentCity)
                startActivity(intent)
            } else {
                Toast.makeText(this, "City not loaded yet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload trip data when returning from edit
        viewModel.getTripById(currentTripId).observe(this) { trip ->
            trip?.let {
                findViewById<TextView>(R.id.tvDetailTripName).text = it.name
                findViewById<TextView>(R.id.tvDetailCity).text = it.city
                currentCity = it.city
                findViewById<TextView>(R.id.tvDetailDates).text =
                    "${dateFormat.format(Date(it.startDate))} - ${dateFormat.format(Date(it.endDate))}"
                findViewById<TextView>(R.id.tvDetailNotes).text =
                    if (it.notes.isNotEmpty()) it.notes else "No notes"

                // Reload weather if city changed
                fetchWeather(it.city,
                    findViewById(R.id.tvWeatherInfo),
                    findViewById(R.id.weatherProgressBar),
                    findViewById(R.id.ivWeatherIcon))
            }
        }
    }

    private fun fetchWeather(city: String, tvWeatherInfo: TextView, progressBar: ProgressBar, ivWeatherIcon: ImageView) {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val response = WeatherRetrofitInstance.weatherApi.getCurrentWeather(city, apiKey)

                val weatherText = """
                    Temperature: ${response.main.temp}°C
                    Feels like: ${response.main.feels_like}°C
                    Humidity: ${response.main.humidity}%
                    Condition: ${response.weather[0].description}
                """.trimIndent()

                tvWeatherInfo.text = weatherText
                tvWeatherInfo.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

                // Load weather icon
                val iconCode = response.weather[0].icon
                val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
                loadWeatherIcon(iconUrl, ivWeatherIcon)
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@TripDetailsActivity, "Failed to load weather", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadWeatherIcon(url: String, imageView: ImageView) {
        lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val connection = java.net.URL(url).openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)

                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                    imageView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteTrip() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete Trip")
            .setMessage("Are you sure you want to delete this trip?")
            .setPositiveButton("Delete") { _, _ ->
                val trip = Trip(
                    id = currentTripId,
                    name = "",
                    city = "",
                    startDate = 0,
                    endDate = 0
                )
                viewModel.deleteTrip(trip)
                Toast.makeText(this, "Trip deleted!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}