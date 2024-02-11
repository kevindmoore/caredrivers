package com.mastertechsoftware.caredrivers.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.util.Locale

/**
 * Adapter to convert everything to lowercase to avoid case issues
 */
class CaseInsensitiveEnumAdapter<T>(values: List<T>) : JsonAdapter<T>() {
  private val _values: Map<String, T> =
    values.associateBy { it.toString().lowercase(Locale.getDefault()) }

  @FromJson
  override fun fromJson(reader: JsonReader): T? {
    val value = reader.nextString().lowercase(Locale.getDefault())
    return _values[value]
  }

  @ToJson
  override fun toJson(writer: JsonWriter, value: T?) {
    writer.value(value.toString().lowercase(Locale.getDefault()))
  }
}
