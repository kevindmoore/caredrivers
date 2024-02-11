package com.mastertechsoftware.caredrivers

import com.mastertechsoftware.caredrivers.network.NetworkService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class CareDriverAPITest {
  @Test
  fun callTripAPI() {
    runBlocking {
      val tripList = NetworkService.careDriverAPI.loadTrips()
      Assert.assertEquals(4, tripList.trips.size)
      Assert.assertEquals(3, tripList.trips[0].passengers.size)
      Assert.assertEquals(3, tripList.trips[0].waypoints.size)
      Assert.assertEquals(2, tripList.trips[0].waypoints[1].passengers.size)

    }
  }
}