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
    private lateinit var etNotes: TextInputEditText

    private var startDateMillis: Long = 0
    private var endDateMillis: Long = 0
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var tripId: Long = 0
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_trip)

        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        etTripName = findViewById(R.id.etTripName)
        etCity = findViewById(R.id.etCity)
        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        etNotes = findViewById(R.id.etNotes)

        // Check if editing existing trip
        tripId = intent.getLongExtra("TRIP_ID", 0)
        isEditMode = tripId != 0L

        if (isEditMode) {
            title = "Edit Trip"
            loadTripData()
        } else {
            title = "Add Trip"
        }

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

        val btnDelete = findViewById<Button>(R.id.btnDeleteTrip)
        if (isEditMode) {
            btnDelete.visibility = android.view.View.VISIBLE
            btnDelete.setOnClickListener {
                deleteTrip()
            }
        } else {
            btnDelete.visibility = android.view.View.GONE
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

    private fun loadTripData() {
        viewModel.getTripById(tripId).observe(this) { trip ->
            trip?.let {
                etTripName.setText(it.name)
                etCity.setText(it.city)
                startDateMillis = it.startDate
                endDateMillis = it.endDate
                etStartDate.setText(dateFormat.format(Date(it.startDate)))
                etEndDate.setText(dateFormat.format(Date(it.endDate)))
                etNotes.setText(it.notes)
            }
        }
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
            id = if (isEditMode) tripId else 0,
            name = name,
            city = city,
            startDate = startDateMillis,
            endDate = endDateMillis,
            notes = etNotes.text.toString().trim()
        )

        if (isEditMode) {
            viewModel.updateTrip(trip)
            Toast.makeText(this, "Trip updated!", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertTrip(trip)
            Toast.makeText(this, "Trip saved!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    private fun deleteTrip() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete Trip")
            .setMessage("Are you sure you want to delete this trip?")
            .setPositiveButton("Delete") { _, _ ->
                val trip = Trip(
                    id = tripId,
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