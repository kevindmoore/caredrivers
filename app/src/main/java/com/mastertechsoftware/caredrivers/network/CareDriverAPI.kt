package com.mastertechsoftware.caredrivers.network

import com.mastertechsoftware.caredrivers.data.models.Gender
import com.mastertechsoftware.caredrivers.data.models.TripList
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.util.Date

/**
 * Interface for the API
 */
interface CareDriverAPI {
  @GET("mobile_coding_challenge_data.json")
  suspend fun loadTrips(
  ): TripList

}

/**
 * Use Retrofit and Moshi for Network calls
 */
object NetworkService {
  private const val BASE_URL = "https://storage.googleapis.com/hsd-interview-resources/"

  private fun provideMoshi(): Moshi =
    Moshi
      .Builder()
      .add(Date::class.java, DateJsonAdapter())
      .add(Gender::class.java, CaseInsensitiveEnumAdapter(Gender.entries)) // Needed because lowercase doesn't work
      .build()


  private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
      .build()
  }

  val careDriverAPI: CareDriverAPI by lazy {
    retrofit.create(CareDriverAPI::class.java)
  }

}