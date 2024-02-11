package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
enum class State(val value: String) {
  @Json(name = "CA") CA("CA");
  companion object {
    fun fromValueOrNull(value: String): State? {
      return State.entries.firstOrNull { it.value == value }
    }
  }

}

