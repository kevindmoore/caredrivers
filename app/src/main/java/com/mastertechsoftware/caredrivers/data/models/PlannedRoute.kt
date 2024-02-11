package com.mastertechsoftware.caredrivers.data.models

import com.mastertechsoftware.caredrivers.network.DateTimeSerializer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import java.util.Date

@JsonClass(generateAdapter = true)
@Serializable
data class PlannedRoute (
  val id: Long,

  @Json(name = "total_time")
  val totalTime: Double,

  @Json(name = "total_distance")
  val totalDistance: Long,

  @Json(name = "starts_at")
  @Serializable(with = DateTimeSerializer::class)
  val startsAt: Date,

  @Json(name = "ends_at")
  @Serializable(with = DateTimeSerializer::class)
  val endsAt: Date,

  @Json(name = "overview_polyline")
  val overviewPolyline: String,

  val legs: List<Leg>
)

