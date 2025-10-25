package com.example.weathertravelplanner.ui.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.liveData
import com.example.weathertravelplanner.data.local.AppDatabase
import com.example.weathertravelplanner.data.local.entity.Trip
import com.example.weathertravelplanner.data.repository.TripRepository
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TripRepository
    val allTrips: LiveData<List<Trip>>

    init {
        val tripDao = AppDatabase.getDatabase(application).tripDao()
        repository = TripRepository(tripDao)
        allTrips = repository.allTrips.asLiveData()
    }

    fun insertTrip(trip: Trip) = viewModelScope.launch {
        repository.insertTrip(trip)
    }

    fun updateTrip(trip: Trip) = viewModelScope.launch {
        repository.updateTrip(trip)
    }

    fun deleteTrip(trip: Trip) = viewModelScope.launch {
        repository.deleteTrip(trip)
    }

    fun getTripById(id: Long): LiveData<Trip?> {
        return liveData {
            val trip = repository.getTripById(id)
            emit(trip)
        }
    }
}