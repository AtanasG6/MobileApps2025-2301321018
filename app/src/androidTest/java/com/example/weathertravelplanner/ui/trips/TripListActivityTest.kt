package com.example.weathertravelplanner.ui.trips

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathertravelplanner.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripListActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TripListActivity::class.java)

    @Test
    fun fabButton_opensAddTripScreen() {
        // Click on FAB button
        onView(withId(R.id.fabAddTrip))
            .check(matches(isDisplayed()))
            .perform(click())

        // Verify AddEditTripActivity is opened by checking for trip name field
        Thread.sleep(500) // Wait for activity to open
        onView(withId(R.id.etTripName))
            .check(matches(isDisplayed()))
    }
}