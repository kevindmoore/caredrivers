package com.mastertechsoftware.caredrivers.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

/**
 * Serializer for Dates
 */
object DateTimeSerializer : KSerializer<Date> {

  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateTime", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Date) {
    encoder.encodeString(dateTimeFormatter.format(value))
  }

  override fun deserialize(decoder: Decoder): Date {
    return dateTimeFormatter.parse(decoder.decodeString())!!
  }
}