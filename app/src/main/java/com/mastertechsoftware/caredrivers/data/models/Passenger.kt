package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Passenger (
  val age: Int,

  @Json(name = "booster_seat")
  val boosterSeat: Boolean,

  @Json(name = "display_name")
  val displayName: String,

  @Json(name = "first_name")
  val firstName: String,

  val initials: String,

  @Json(name = "must_be_met")
  val mustBeMet: Boolean? = null,

  @Json(name = "front_seat_authorized")
  val frontSeatAuthorized: Boolean,

  @Json(name = "date_of_birth")
  val dateOfBirth: String,

  val gender: Gender,

  @Json(name = "rider_notes")
  val riderNotes: String,

  val slug: String,
  val uuid: String
)