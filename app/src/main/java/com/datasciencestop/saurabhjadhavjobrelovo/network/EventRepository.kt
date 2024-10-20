package com.datasciencestop.saurabhjadhavjobrelovo.network

import com.datasciencestop.saurabhjadhavjobrelovo.model.Event
import com.datasciencestop.saurabhjadhavjobrelovo.network.api.PlacesApiService
import com.datasciencestop.saurabhjadhavjobrelovo.network.model.PlacesResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventRepository {

    private val apiKey = "AIzaSyAOVYRIgupAurZup5y1PRh8Ismb1A3lLao" // Replace with your actual API key

    private val api: PlacesApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Set the desired log level (BODY, BASIC, HEADERS, or NONE)
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Step 2b: Create an OkHttpClient and add the logging interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Step 2c: Build Retrofit with the OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .client(client) // Add the OkHttpClient here
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PlacesApiService::class.java)
    }

    suspend fun getEvents(location: LatLng): List<Event> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getNearbyPlaces(
                    location = "${location.latitude},${location.longitude}",
                    radius = 5000, // 5 km radius
                    keyword = "events",
                    key = apiKey
                )

                if (response.isSuccessful) {
                    val placesResponse: PlacesResponse? = response.body()
                    val places = placesResponse?.results ?: emptyList()
                    places.map { place ->
                        Event(
                            id = place.place_id,
                            title = place.name ?: "Unnamed Event",
                            description = place.vicinity ?: "",
                            dateTime = "", // DateTime not provided by Places API
                            category = place.types?.firstOrNull() ?: "Event",
                            location = LatLng(
                                place.geometry.location.lat,
                                place.geometry.location.lng
                            ),
                            distance = place.distanceMeters / 1000.0 // Convert to km if available
                        )
                    }
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
