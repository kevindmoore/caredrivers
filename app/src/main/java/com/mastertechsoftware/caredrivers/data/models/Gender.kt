package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
enum class Gender(val value: String) {
  @Json(name = "female")
  Female("female"),
  @Json(name = "male")
  Male("male");

  companion object {
    fun fromValueOrNull(value: String): Gender? {
      return entries.firstOrNull { it.value == value }
    }
  }
}

