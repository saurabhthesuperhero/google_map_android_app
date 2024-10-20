package com.datasciencestop.saurabhjadhavjobrelovo.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.datasciencestop.saurabhjadhavjobrelovo.viewmodel.EventViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun EventsMapView(navController: NavController, viewModel: EventViewModel) {
    val events = viewModel.events.collectAsState().value
    val context = LocalContext.current

    // State to hold the user's current location
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // Obtain the Fused Location Provider Client
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Check for location permissions and get the user's location
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)

                    // Fetch events based on user's current location
                    viewModel.fetchEvents(userLocation!!)
                }
            }
        } else {
            // Handle the case where permission is not granted
            // You might want to request permission here or show a message
        }
    }

    // Set the camera position based on the user's location or default location
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation ?: LatLng(37.7749, -122.4194), // Default to San Francisco
            12f
        )
    }

    // Update camera position when userLocation changes
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 12f)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(myLocationButtonEnabled = true),
        properties = MapProperties(isMyLocationEnabled = userLocation != null)
    ) {
        events.forEach { event ->
            Marker(
                state = rememberMarkerState(position = event.location),
                title = event.title,
                snippet = event.description,
                onClick = {
                    navController.navigate("event_details/${event.id}")
                    true
                }
            )
        }
    }
}
