package com.mastertechsoftware.caredrivers.data.models

import com.mastertechsoftware.caredrivers.network.DateTimeSerializer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import java.util.Date

@JsonClass(generateAdapter = true)
@Serializable
data class Waypoint (
  val id: Long,

  @Json(name = "account_locations")
  val accountLocations: List<AccountLocation>,

  @Json(name = "estimated_arrives_at")
  @Serializable(with = DateTimeSerializer::class)
  val estimatedArrivesAt: Date,

  // TODO: Need clarification on contents of instructions
//  @Json(name = "rider_location_instructions")
//  val riderLocationInstructions: RiderLocationInstructions,

  val location: Location,
  val passengers: List<Passenger>,
  val instructions: Instructions? = null
)
