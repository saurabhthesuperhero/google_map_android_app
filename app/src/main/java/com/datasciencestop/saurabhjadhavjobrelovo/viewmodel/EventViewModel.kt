package com.datasciencestop.saurabhjadhavjobrelovo.viewmodel

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasciencestop.saurabhjadhavjobrelovo.model.Event
import com.datasciencestop.saurabhjadhavjobrelovo.network.EventRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class EventViewModel : ViewModel() {

    private val repository = EventRepository()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    var selectedCategory by mutableStateOf("All")
    var startDate by mutableStateOf("")
    var endDate by mutableStateOf("")
    var isCategoryDropdownExpanded by mutableStateOf(false)

    private var userLocation: LatLng? = null


    fun fetchEvents(userLocation: LatLng) {
        this.userLocation = userLocation
        viewModelScope.launch {
            Log.d("EventViewModel", "Fetching events for user location: $userLocation")
            val eventList = repository.getEvents(userLocation)
            Log.d("EventViewModel", "Fetched events: $eventList")
            _events.value = applyFiltersToEvents(eventList)
        }
    }

    fun applyFilters(selectedCategory: String, startDate: String, endDate: String) {
        this.selectedCategory = selectedCategory
        this.startDate = startDate
        this.endDate = endDate

        // Re-fetch or re-filter events based on new filters
        userLocation?.let {
            fetchEvents(it)
        }
    }

    private fun applyFiltersToEvents(eventList: List<Event>): List<Event> {
        var filteredEvents = eventList

        if (selectedCategory != "All") {
            filteredEvents = filteredEvents.filter { event ->
                event.category.equals(selectedCategory, ignoreCase = true)
            }
        }

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateObj = if (startDate.isNotEmpty()) dateFormatter.parse(startDate) else null
        val endDateObj = if (endDate.isNotEmpty()) dateFormatter.parse(endDate) else null

        if (startDateObj != null || endDateObj != null) {
            filteredEvents = filteredEvents.filter { event ->
                val eventDateObj = if (event.dateTime.isNotEmpty())
                    dateFormatter.parse(event.dateTime.substring(0, 10)) else null

                if (eventDateObj != null) {
                    val afterStart = startDateObj?.let { eventDateObj >= it } ?: true
                    val beforeEnd = endDateObj?.let { eventDateObj <= it } ?: true
                    afterStart && beforeEnd
                } else {
                    true
                }
            }
        }

        return filteredEvents
    }


}
