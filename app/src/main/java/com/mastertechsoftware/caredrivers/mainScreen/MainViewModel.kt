package com.mastertechsoftware.caredrivers.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastertechsoftware.caredrivers.data.models.Location
import com.mastertechsoftware.caredrivers.data.models.Trip
import com.mastertechsoftware.caredrivers.data.models.Waypoint
import com.mastertechsoftware.caredrivers.network.NetworkService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class StartEndDate(val startDate: Date, val endDate: Date)
data class TripDate(val date: Date, val trips: List<Trip>)

/**
 * This is the viewmodel that is used throughout the app. Contains utility methods
 */
class MainViewModel : ViewModel() {

  companion object {
    // Dates formatting
    val dayMonthFormatter = SimpleDateFormat("EEE M/d", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("hh:mma", Locale.getDefault())

    /**
     * Find the start & end dates for a list of trips
     * @param trips
     */
    fun getStartEndDate(trips: List<Trip>): StartEndDate {
      return StartEndDate(
        startDate = trips[0].plannedRoute.startsAt,
        endDate = trips[trips.size - 1].plannedRoute.endsAt
      )
    }

    /**
     * Get the Account Locations for the given trip
     * @param trip
     */
    fun getTripAccountLocations(trip: Trip): List<Location> {
      val accountLocations = mutableListOf<Location>()
      val wayPoints = orderWayPoints(trip)
      wayPoints.map {
        accountLocations.add(it.location)
      }
      return accountLocations
    }

    fun getLatestEndDate(trip: Trip): Date {
      var latestEndDate = trip.waypoints[0].estimatedArrivesAt
      for (waypoint in trip.waypoints) {
        if (waypoint.estimatedArrivesAt.after(latestEndDate)) {
          latestEndDate = waypoint.estimatedArrivesAt
        }
      }
      return latestEndDate
    }


    /**
     * Since it seems that waypoints are ordered byt how they appear in legs and the
     * waypoints don't seem to be in the proper order, get an ordered list of waypoints
     * @param trip Trip
     */
    fun orderWayPoints(trip: Trip): List<Waypoint> {
      val orderedWayPoints = mutableSetOf<Waypoint>()
      trip.plannedRoute.legs.map {
        var wayPoint = findWayPoint(trip, it.startWaypointID)
        if (wayPoint != null) {
          orderedWayPoints.add(wayPoint)
        }
        wayPoint = findWayPoint(trip, it.endWaypointID)
        if (wayPoint != null) {
          orderedWayPoints.add(wayPoint)
        }
      }
      return orderedWayPoints.toList()
    }

    /**
     * Find the waypoint with the given id
     * @param trip
     * @param id waypoint id
     */
    fun findWayPoint(trip: Trip, id: Long): Waypoint? {
      return trip.waypoints.firstOrNull {
        it.id == id
      }
    }

    /**
     * Convert a string from PM to p and AM to a since SimpleDateFormat can't do this
     */
    fun convertAMPM(dateString: String): String {
      if (dateString.endsWith("PM")) {
        return dateString.substring(0, dateString.indexOf("PM")) + "p"
      } else {
        return dateString.substring(0, dateString.indexOf("AM")) + "a"
      }
    }

    /**
     * Go through the list of trips and add up all the earnings
     * @param trips
     */
    fun totalTripAmount(trips: List<Trip>): Long {
      return trips.fold(0L) { accumulator, trip -> (accumulator + trip.estimatedEarnings) }
    }

    /**
     * Convert meters to Miles
     */
    fun convertMetersToMiles(meters: Long): Double {
      return meters * 0.000621371
    }

    /**
     * Zero out time for date comparisons
     */
    fun dateToDay(date: Date): Date {
      val calendar = Calendar.getInstance()
      calendar.time = date
      calendar.set(Calendar.HOUR, 0)
      calendar.set(Calendar.MINUTE, 0)
      calendar.set(Calendar.MILLISECOND, 0)
      return calendar.time
    }
  }

  private val _currentTripList = MutableLiveData<List<TripDate>?>(null)
  val currentTripList: LiveData<List<TripDate>?> = _currentTripList

  /**
   * Load the Trip List from our network class
   */
  fun loadTripList() {
    viewModelScope.launch {
      val tripList = NetworkService.careDriverAPI.loadTrips()
      // Sort by start date
      val sortedTripList = tripList.trips.sortedBy {
        it.plannedRoute.startsAt
      }
      // Go thru each trip and create a map by date.
      // Note that since dates have time values, those need to be stripped
      // to allow equality
      val tripMap = mutableMapOf<Date, MutableList<Trip>>()
      sortedTripList.map {
        val date = dateToDay(it.plannedRoute.startsAt)
        if (tripMap.containsKey(date)) {
          var trips = tripMap[date]
          if (trips == null) {
            trips = mutableListOf()
          }
          trips.add(it)
        } else {
          val trips = mutableListOf<Trip>()
          tripMap[date] = trips
          trips.add(it)
        }
      }
      val tripMapList = mutableListOf<TripDate>()
      tripMap.map {
        tripMapList.add(TripDate(it.key, it.value))
      }
      _currentTripList.value = tripMapList
    }
  }

}