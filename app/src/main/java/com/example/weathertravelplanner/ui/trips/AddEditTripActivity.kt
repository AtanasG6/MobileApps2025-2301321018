package com.example.weathertravelplanner.ui.trips

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weathertravelplanner.R
import com.example.weathertravelplanner.data.local.entity.Trip
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddEditTripActivity : AppCompatActivity() {

    private lateinit var viewModel: TripViewModel
    private lateinit var etTripName: TextInputEditText
    private lateinit var etCity: TextInputEditText
    private lateinit var etStartDate: TextInputEditText
    private lateinit var etEndDate: TextInputEditText

    private var startDateMillis: Long = 0
    private var endDateMillis: Long = 0
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_trip)

        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        etTripName = findViewById(R.id.etTripName)
        etCity = findViewById(R.id.etCity)
        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)

        etStartDate.setOnClickListener {
            showDatePicker { date ->
                startDateMillis = date
                etStartDate.setText(dateFormat.format(Date(date)))
            }
        }

        etEndDate.setOnClickListener {
            showDatePicker { date ->
                endDateMillis = date
                etEndDate.setText(dateFormat.format(Date(date)))
            }
        }

        findViewById<Button>(R.id.btnSaveTrip).setOnClickListener {
            saveTrip()
        }
    }

    private fun showDatePicker(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveTrip() {
        val name = etTripName.text.toString().trim()
        val city = etCity.text.toString().trim()

        if (name.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (startDateMillis == 0L || endDateMillis == 0L) {
            Toast.makeText(this, "Please select dates", Toast.LENGTH_SHORT).show()
            return
        }

        if (endDateMillis < startDateMillis) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show()
            return
        }

        val trip = Trip(
            name = name,
            city = city,
            startDate = startDateMillis,
            endDate = endDateMillis,
            notes = ""
        )

        viewModel.insertTrip(trip)
        Toast.makeText(this, "Trip saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}