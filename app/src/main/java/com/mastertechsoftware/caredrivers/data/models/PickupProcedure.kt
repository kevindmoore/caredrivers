package com.mastertechsoftware.caredrivers.data.models

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class PickupProcedure (
  val time: Long,
  val instructions: String? = null
)

