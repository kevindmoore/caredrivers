package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
enum class Instructions(val value: String) {
  @Json(name = "na")
  Na("na"),
  @Json(name = "on the side")
  OnTheSide("on the side"),
  @Json(name = "test")
  Test("test");

  companion object {
    fun fromValueOrNull(value: String): Instructions? {
      return Instructions.entries.firstOrNull { it.value == value }
    }
  }
}

