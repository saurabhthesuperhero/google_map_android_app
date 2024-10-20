package com.datasciencestop.saurabhjadhavjobrelovo.model


import com.google.android.gms.maps.model.LatLng

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val dateTime: String,
    val category: String,
    val location: LatLng,
    val distance: Double
)
