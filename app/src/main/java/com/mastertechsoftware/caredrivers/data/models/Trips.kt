package com.mastertechsoftware.caredrivers.data.models

import com.mastertechsoftware.caredrivers.network.DateTimeSerializer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.*
import java.util.Date

@JsonClass(generateAdapter = true)
@Serializable
data class TripList (
  val trips: List<Trip>
)

@JsonClass(generateAdapter = true)
@Serializable
data class Trip (
  val carpool: Boolean,
  val claimable: Boolean,

  @Json(name = "driver_can_cancel")
  val driverCanCancel: Boolean,

  @Json(name = "driver_fare_multiplier")
  val driverFareMultiplier: Long,

  @Json(name = "driver_view_permission")
  val driverViewPermission: String,

  @Json(name = "estimated_earnings")
  val estimatedEarnings: Long,

  @Json(name = "estimated_net_earnings")
  val estimatedNetEarnings: Long,

  val id: Long,
  val slug: String,
  val state: String,

  @Json(name = "time_anchor")
  val timeAnchor: String,

  @Json(name = "trip_opportunity")
  val tripOpportunity: Boolean,

  @Json(name = "updated_at")
  @Serializable(with = DateTimeSerializer::class)
  val updatedAt: Date,

  val shuttle: Boolean,

  @Json(name = "in_series")
  val inSeries: Boolean? = null,

  val passengers: List<Passenger>,

  @Json(name = "payment_starts_at")
  @Serializable(with = DateTimeSerializer::class)
  val paymentStartsAt: Date,

  @Json(name = "payment_ends_at")
  @Serializable(with = DateTimeSerializer::class)
  val paymentEndsAt: Date,

  // TODO Get more details
//  @Json(name = "required_driver_qualifications")
//  val requiredDriverQualifications: JsonArray,

  @Json(name = "time_zone_name")
  val timeZoneName: String,

//  val tags: JsonArray,
  val uuid: String,

//  @Json(name = "promotion_uuids")
//  val promotionUuids: JsonArray,

  @Json(name = "in_cart")
  val inCart: Boolean,

  @Json(name = "planned_route")
  val plannedRoute: PlannedRoute,

  val waypoints: List<Waypoint>,

  @Json(name = "trip_template_id")
  val tripTemplateID: Long? = null
)




