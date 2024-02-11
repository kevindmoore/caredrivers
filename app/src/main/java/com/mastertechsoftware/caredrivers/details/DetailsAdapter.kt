package com.mastertechsoftware.caredrivers.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mastertechsoftware.caredrivers.R
import com.mastertechsoftware.caredrivers.data.models.Location
import com.mastertechsoftware.caredrivers.data.models.Trip
import com.mastertechsoftware.caredrivers.databinding.DetailsRowBinding
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.getTripAccountLocations

/**
 * Adapter for the Details page. Shows the routes for the trip
 */
class DetailsAdapter(
  private val trip: Trip,
) :
  RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {
    private var accountLocations: List<Location> = getTripAccountLocations(trip)

  inner class DetailsViewHolder(private val binding: DetailsRowBinding) : ViewHolder(binding.root) {

    /**
     * Bind the location
     */
    fun bind(accountLocation: Location, position: Int) {
      binding.dropOffLocation.text = accountLocation.address
      // Show the icon & text
      if (trip.timeAnchor == "drop_off") {
        when (position) {
          0 -> {
            setPickup()
            setAnchor()
          }
          accountLocations.size - 1 -> {
            setDropOff()
            setAnchor()
          }
          else -> {
            setDropOff()
            setNonAnchor()
          }
        }
      } else {
        when (position) {
          0 -> {
            setPickup()
            setAnchor()
          }
          accountLocations.size - 1 -> {
            setDropOff()
            setAnchor()
          }
          else -> {
            setPickup()
            setNonAnchor()
          }
        }
      }
    }
    private fun setDropOff() {
      binding.dropOffLabel.text = binding.root.context.getString(R.string.dropOff)
    }

    private fun setPickup() {
      binding.dropOffLabel.text = binding.root.context.getString(R.string.pickup)
    }

    private fun setAnchor() {
      binding.iconType.setImageResource(R.drawable.diamond)
      binding.iconType.rotation = 45f
    }

    private fun setNonAnchor() {
      binding.iconType.setImageResource(R.drawable.circle)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
      return DetailsViewHolder(DetailsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun getItemCount(): Int {
    return accountLocations.size
  }

  override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
    holder.bind(accountLocations[position], position)
  }

}