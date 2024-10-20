package com.datasciencestop.saurabhjadhavjobrelovo.network.api

import com.datasciencestop.saurabhjadhavjobrelovo.network.model.PlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String, // "latitude,longitude"
        @Query("radius") radius: Int, // in meters
        @Query("keyword") keyword: String, // e.g., "events"
        @Query("key") key: String
    ): Response<PlacesResponse>
}
