package com.example.weathertravelplanner.ui.trips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertravelplanner.R
import com.example.weathertravelplanner.data.local.entity.Trip
import java.text.SimpleDateFormat
import java.util.*

class TripAdapter(
    private var trips: List<Trip>,
    private val onTripClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTripName: TextView = view.findViewById(R.id.tvTripName)
        val tvTripCity: TextView = view.findViewById(R.id.tvTripCity)
        val tvTripDates: TextView = view.findViewById(R.id.tvTripDates)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        holder.tvTripName.text = trip.name
        holder.tvTripCity.text = trip.city
        holder.tvTripDates.text = formatDates(trip.startDate, trip.endDate)

        holder.itemView.setOnClickListener {
            onTripClick(trip)
        }
    }

    override fun getItemCount() = trips.size

    fun updateTrips(newTrips: List<Trip>) {
        trips = newTrips
        notifyDataSetChanged()
    }

    private fun formatDates(startDate: Long, endDate: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return "${sdf.format(Date(startDate))} - ${sdf.format(Date(endDate))}"
    }
}