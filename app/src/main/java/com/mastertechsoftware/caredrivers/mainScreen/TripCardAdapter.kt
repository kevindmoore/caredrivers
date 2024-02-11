package com.mastertechsoftware.caredrivers.mainScreen

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mastertechsoftware.caredrivers.R
import com.mastertechsoftware.caredrivers.data.models.Trip
import com.mastertechsoftware.caredrivers.databinding.TripCardBinding
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.convertAMPM
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.getLatestEndDate
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.getTripAccountLocations
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.timeFormatter
import timber.log.Timber
import java.text.ParseException

/**
 * Adapter for the list of trips
 */
class TripCardAdapter(
  private val trips: List<Trip>,
  private val itemClickListener: (Trip) -> Unit
) :
  RecyclerView.Adapter<TripCardAdapter.TripCardViewHolder>() {
  /**
   * Card Holder
   */
  inner class TripCardViewHolder(private val tripCardBinding: TripCardBinding) :
    ViewHolder(tripCardBinding.root) {

    fun bind(trip: Trip, position: Int) {
      val accountLocations = getTripAccountLocations(trip)
      try {
        // build up the title row values
        val startTime = trip.plannedRoute.startsAt
        val endTime = getLatestEndDate(trip)
        val startEndTime = tripCardBinding.root.context.getString(
          R.string.time_range,
          convertAMPM(
            timeFormatter.format(startTime)
          ),
          convertAMPM(timeFormatter.format(endTime))
        )
        val estimatedAmount = tripCardBinding.root.context.getString(
          R.string.est_amount,
          trip.estimatedEarnings,
        )
        val resources = tripCardBinding.root.context.resources
        val numRiders = resources.getQuantityString(
          R.plurals.riders,
          trip.passengers.size,
          trip.passengers.size
        )
        val boosters = trip.passengers.fold(0) { accumulator, passenger -> accumulator + if (passenger.boosterSeat) 1 else 0 }
        val numBoosters = resources.getQuantityString(
          R.plurals.boosters,
          boosters,
          boosters
        )
        var riderText = resources.getString(R.string.rider, numRiders)
        if (boosters > 0) {
          riderText = resources.getString(R.string.rider_and_booster, numRiders, numBoosters)
        }
        val rideTime = resources.getString(R.string.time_range_plus_rider, startEndTime, riderText)
        val boldText = SpannableString(rideTime)
        boldText.setSpan(StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tripCardBinding.rideTime.setText(boldText, TextView.BufferType.SPANNABLE)
        tripCardBinding.ridePrice.text = estimatedAmount
      } catch (e: ParseException) {
        Timber.e(e, "Problems parsing ${trip.paymentStartsAt}")
      }
      tripCardBinding.waypoints.also {
        it.layoutManager = LinearLayoutManager(tripCardBinding.root.context)
        it.adapter = RouteAdapter(accountLocations) {
          itemClickListener(trips[position])
        }
      }
      tripCardBinding.overlay.setOnClickListener {
        itemClickListener(trips[position])
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripCardViewHolder {
    val tripCardBinding =
      TripCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return TripCardViewHolder(tripCardBinding)
  }

  override fun getItemCount(): Int {
    return trips.size
  }

  override fun onBindViewHolder(holder: TripCardViewHolder, position: Int) {
    holder.bind(trips[position], position)
  }

}