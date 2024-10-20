package com.datasciencestop.saurabhjadhavjobrelovo.network.model

import com.google.gson.annotations.SerializedName

data class PlacesResponse(
    val results: List<PlaceResult>
)

data class PlaceResult(
    val place_id: String,
    val name: String?,
    val vicinity: String?,
    val geometry: Geometry,
    val types: List<String>?,
    @SerializedName("distance_meters") val distanceMeters: Int = 0
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
