package com.example.weathertravelplanner.ui.trips

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertravelplanner.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TripListActivity : AppCompatActivity() {

    private lateinit var viewModel: TripViewModel
    private lateinit var adapter: TripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTrips)
        adapter = TripAdapter(emptyList()) { trip ->
            // TODO: Handle trip click
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe trips
        viewModel.allTrips.observe(this) { trips ->
            adapter.updateTrips(trips)
        }

        // Setup FAB
        findViewById<FloatingActionButton>(R.id.fabAddTrip).setOnClickListener {
            val intent = android.content.Intent(this, AddEditTripActivity::class.java)
            startActivity(intent)
        }
    }
}