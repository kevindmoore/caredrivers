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
import com.mastertechsoftware.caredrivers.databinding.MyRidesBinding
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.convertAMPM
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.dayMonthFormatter
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.timeFormatter
import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException

/**
 * Adapter for the top level list of TripDate classes
 */
class TripsAdapter(
  private val trips: List<TripDate>,
  private val itemClickListener: (Trip) -> Unit
) :
  RecyclerView.Adapter<TripsAdapter.TripViewHolder>() {

  inner class TripViewHolder(private val myRidesBinding: MyRidesBinding) : ViewHolder(myRidesBinding.root) {

    fun bind(tripDate: TripDate) {
      try {
        val startEndDate = MainViewModel.getStartEndDate(tripDate.trips)
        myRidesBinding.header.rideDate.text = dayMonthFormatter.format(startEndDate.startDate)
        val startEndTime = myRidesBinding.root.context.getString(
          R.string.time_range,
          convertAMPM(timeFormatter.format(startEndDate.startDate)),
          convertAMPM(timeFormatter.format(startEndDate.endDate))
        )
        val boldText = SpannableString(startEndTime)
        boldText.setSpan(StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        myRidesBinding.header.rideTime.setText(boldText, TextView.BufferType.SPANNABLE)
      } catch (e: ParseException) {
        Timber.e(e, "Problems parsing")
      }
      myRidesBinding.trips.also {
        it.layoutManager = LinearLayoutManager(myRidesBinding.root.context)
        it.adapter = TripCardAdapter(tripDate.trips, itemClickListener = itemClickListener)
      }
      myRidesBinding.header.estimatedAmount.text =
        NumberFormat.getCurrencyInstance().format(MainViewModel.totalTripAmount(tripDate.trips))
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
    val myRidesBinding = MyRidesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return TripViewHolder(myRidesBinding)
  }

  override fun getItemCount(): Int {
    return trips.size
  }

  override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
    holder.bind(trips[position])
  }

}