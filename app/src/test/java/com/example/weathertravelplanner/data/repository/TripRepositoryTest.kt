package com.example.weathertravelplanner.data.repository

import com.example.weathertravelplanner.data.local.dao.TripDao
import com.example.weathertravelplanner.data.local.entity.Trip
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class TripRepositoryTest {

    private lateinit var tripDao: TripDao
    private lateinit var repository: TripRepository

    @Before
    fun setup() {
        tripDao = mock(TripDao::class.java)
        repository = TripRepository(tripDao)
    }

    @Test
    fun insertTrip_callsDaoInsert() {
        runBlocking {
            val trip = Trip(1, "Test Trip", "Sofia", 0L, 1L, "Notes")

            `when`(tripDao.insertTrip(trip)).thenReturn(1L)

            val result = repository.insertTrip(trip)

            assertEquals(1L, result)
            verify(tripDao).insertTrip(trip)
        }
    }

    @Test
    fun deleteTrip_callsDaoDelete() {
        runBlocking {
            val trip = Trip(1, "Test Trip", "Sofia", 0L, 1L, "Notes")

            repository.deleteTrip(trip)

            verify(tripDao).deleteTrip(trip)
        }
    }
}