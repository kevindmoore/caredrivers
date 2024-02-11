package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Leg (
  val id: Long,
  val slug: String,

  @Json(name = "starts_at")
  val startsAt: String,

  @Json(name = "ends_at")
  val endsAt: String,

  val position: Long,

  @Json(name = "start_waypoint_id")
  val startWaypointID: Long,

  @Json(name = "end_waypoint_id")
  val endWaypointID: Long
)

