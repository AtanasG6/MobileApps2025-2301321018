package com.example.weathertravelplanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weathertravelplanner.ui.trips.TripListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redirect to TripListActivity
        val intent = Intent(this, TripListActivity::class.java)
        startActivity(intent)
        finish()
    }
}