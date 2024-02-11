package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Location (
  val id: Long,
  val address: String,
  val lat: Double,
  val lng: Double,

  @Json(name = "place_id")
  val placeID: String,

  @Json(name = "street_address")
  val streetAddress: String,

  @Json(name = "street_name")
  val streetName: String,

  @Json(name = "street_number")
  val streetNumber: String,

  val city: String,
  val zipcode: String,
  val state: State
)

