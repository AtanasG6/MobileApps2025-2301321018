package com.example.weathertravelplanner.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weathertravelplanner.R
import com.example.weathertravelplanner.data.remote.api.RetrofitInstance
import com.example.weathertravelplanner.ui.trips.TripViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: TripViewModel
    private val apiKey = "06c5fcf1013fc1260a9a1213e2630e08"
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        val tripId = intent.getLongExtra("TRIP_ID", 0)

        val tvTripName = findViewById<TextView>(R.id.tvDetailTripName)
        val tvCity = findViewById<TextView>(R.id.tvDetailCity)
        val tvDates = findViewById<TextView>(R.id.tvDetailDates)
        val tvNotes = findViewById<TextView>(R.id.tvDetailNotes)
        val tvWeatherInfo = findViewById<TextView>(R.id.tvWeatherInfo)
        val progressBar = findViewById<ProgressBar>(R.id.weatherProgressBar)

        viewModel.getTripById(tripId).observe(this) { trip ->
            trip?.let {
                tvTripName.text = it.name
                tvCity.text = it.city
                tvDates.text = "${dateFormat.format(Date(it.startDate))} - ${dateFormat.format(Date(it.endDate))}"
                tvNotes.text = if (it.notes.isNotEmpty()) it.notes else "No notes"

                // Fetch weather
                fetchWeather(it.city, tvWeatherInfo, progressBar)
            }
        }

        findViewById<Button>(R.id.btnViewOnMap).setOnClickListener {
            // TODO: Open map
            Toast.makeText(this, "Map coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchWeather(city: String, tvWeatherInfo: TextView, progressBar: ProgressBar) {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val response = RetrofitInstance.weatherApi.getCurrentWeather(city, apiKey)

                val weatherText = """
                    Temperature: ${response.main.temp}°C
                    Feels like: ${response.main.feels_like}°C
                    Humidity: ${response.main.humidity}%
                    Condition: ${response.weather[0].description}
                """.trimIndent()

                tvWeatherInfo.text = weatherText
                tvWeatherInfo.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@TripDetailsActivity, "Failed to load weather", Toast.LENGTH_SHORT).show()
            }
        }
    }
}