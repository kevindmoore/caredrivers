package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class AccountLocation (
  @Json(name = "account_id")
  val accountID: Long,

  val address: String,
  val id: Long,
  val lat: Double,
  val lng: Double,

  @Json(name = "pickup_procedure")
  val pickupProcedure: PickupProcedure,

  @Json(name = "pickup_procedure_time")
  val pickupProcedureTime: Long,

  @Json(name = "dropoff_procedure_time")
  val dropoffProcedureTime: Long,

  @Json(name = "place_id")
  val placeID: String,

  @Json(name = "pickup_procedure_instructions")
  val pickupProcedureInstructions: String? = null,

  @Json(name = "dropoff_procedure_instructions")
  val dropoffProcedureInstructions: Instructions? = null
)

