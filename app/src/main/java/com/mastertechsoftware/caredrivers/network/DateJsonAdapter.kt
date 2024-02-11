package com.mastertechsoftware.caredrivers.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.text.SimpleDateFormat
import java.util.Date

val dateTimeFormatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

/**
 * Adapter for Dates
 */
class DateJsonAdapter: JsonAdapter<Date>() {
  override fun fromJson(reader: JsonReader): Date {
    val dateString = reader.nextString()
    return dateTimeFormatter.parse(dateString)!!
  }

  override fun toJson(writer: JsonWriter, dateTime: Date?) {
    if (dateTime != null) {
      writer.value(dateTimeFormatter.format(dateTime))
    }
  }
}