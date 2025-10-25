package com.example.weathertravelplanner.data.repository

import com.example.weathertravelplanner.data.local.dao.TripDao
import com.example.weathertravelplanner.data.local.entity.Trip
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripDao: TripDao) {

    val allTrips: Flow<List<Trip>> = tripDao.getAllTrips()

    suspend fun getTripById(id: Long): Trip? {
        return tripDao.getTripById(id)
    }

    suspend fun insertTrip(trip: Trip): Long {
        return tripDao.insertTrip(trip)
    }

    suspend fun updateTrip(trip: Trip) {
        tripDao.updateTrip(trip)
    }

    suspend fun deleteTrip(trip: Trip) {
        tripDao.deleteTrip(trip)
    }
}