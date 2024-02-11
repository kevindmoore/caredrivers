package com.mastertechsoftware.caredrivers.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mastertechsoftware.caredrivers.R
import com.mastertechsoftware.caredrivers.data.models.Location
import com.mastertechsoftware.caredrivers.databinding.RouteRowBinding

/**
 * Adapter for the route Row. Just shows a numbered Address
 */
class RouteAdapter(private val accountLocations: List<Location>, private val itemClickListener: () -> Unit) :
  RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

  inner class RouteViewHolder(val routeRow: RouteRowBinding) :
    RecyclerView.ViewHolder(routeRow.root) {

    fun bind(position: Int) {
      routeRow.routeInfo.text = routeRow.root.context.getString(
        R.string.route,
        position+1,
        accountLocations[position].address
      )
      routeRow.routeInfo.setOnClickListener {
        itemClickListener()
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
    val routeRowBinding =
      RouteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RouteViewHolder(routeRowBinding)
  }

  override fun getItemCount(): Int {
    return accountLocations.size
  }

  override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
    holder.bind(position)
  }

}