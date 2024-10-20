package com.datasciencestop.saurabhjadhavjobrelovo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.datasciencestop.saurabhjadhavjobrelovo.model.Event
import com.datasciencestop.saurabhjadhavjobrelovo.viewmodel.EventViewModel

@Composable
fun EventsListView(navController: NavController, viewModel: EventViewModel) {
    val events = viewModel.events.collectAsState().value

    LazyColumn {
        items(events.size) { index ->
            val event = events[index]
            EventListItem(event) {
                navController.navigate("event_details/${event.id}")
            }
            Divider()
        }
    }
}

@Composable
fun EventListItem(event: Event, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = event.title, style = MaterialTheme.typography.titleLarge)
        Text(text = event.dateTime, style = MaterialTheme.typography.bodyMedium)
        Text(text = event.category, style = MaterialTheme.typography.bodySmall)
    }
}
