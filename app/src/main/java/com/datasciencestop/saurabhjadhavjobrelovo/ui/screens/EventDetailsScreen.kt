package com.datasciencestop.saurabhjadhavjobrelovo.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.datasciencestop.saurabhjadhavjobrelovo.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    navController: NavController,
    eventId: String?,
    viewModel: EventViewModel = EventViewModel()
) {
    val event = viewModel.events.collectAsState().value.find { it.id == eventId }
    val context = LocalContext.current

    event?.let {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(it.title) })
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text(text = it.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Date & Time: ${it.dateTime}")
                Text(text = "Category: ${it.category}")
                Text(text = "Distance: ${it.distance} km")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val gmmIntentUri =
                        Uri.parse("geo:${it.location.latitude},${it.location.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                }) {
                    Text("Open in Google Maps")
                }
            }
        }
    } ?: Text("Event not found")
}
