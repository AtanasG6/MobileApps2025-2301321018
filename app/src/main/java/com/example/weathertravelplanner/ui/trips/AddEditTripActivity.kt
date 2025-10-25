package com.example.weathertravelplanner.ui.trips

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weathertravelplanner.R
import com.example.weathertravelplanner.data.local.entity.Trip
import com.google.android.material.textfield.TextInputEditText

class AddEditTripActivity : AppCompatActivity() {

    private lateinit var viewModel: TripViewModel
    private lateinit var etTripName: TextInputEditText
    private lateinit var etCity: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_trip)

        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        etTripName = findViewById(R.id.etTripName)
        etCity = findViewById(R.id.etCity)

        findViewById<Button>(R.id.btnSaveTrip).setOnClickListener {
            saveTrip()
        }
    }

    private fun saveTrip() {
        val name = etTripName.text.toString().trim()
        val city = etCity.text.toString().trim()

        if (name.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val trip = Trip(
            name = name,
            city = city,
            startDate = System.currentTimeMillis(),
            endDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L), // +7 days
            notes = ""
        )

        viewModel.insertTrip(trip)
        Toast.makeText(this, "Trip saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}