package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class RiderLocationInstructions (
  @Json(name = "af1863903c4b44")
  val af1863903C4B44: String? = null
)
