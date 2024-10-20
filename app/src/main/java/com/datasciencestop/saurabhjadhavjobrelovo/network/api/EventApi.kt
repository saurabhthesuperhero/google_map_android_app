package com.datasciencestop.saurabhjadhavjobrelovo.network.api

import com.datasciencestop.saurabhjadhavjobrelovo.model.Event
import retrofit2.http.GET

interface EventApi {
    @GET("events")
    suspend fun fetchEvents(): List<Event>
}
