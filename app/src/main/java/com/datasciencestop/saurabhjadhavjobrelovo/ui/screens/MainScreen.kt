package com.datasciencestop.saurabhjadhavjobrelovo.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.datasciencestop.saurabhjadhavjobrelovo.ui.components.EventsListView
import com.datasciencestop.saurabhjadhavjobrelovo.ui.components.EventsMapView
import com.datasciencestop.saurabhjadhavjobrelovo.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: EventViewModel = EventViewModel()) {
    var isMapView by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Local Events") },
                actions = {
                    IconButton(onClick = { isMapView = !isMapView }) {
                        Icon(
                            imageVector = if (isMapView) Icons.Default.List else Icons.Default.Map,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { navController.navigate("filter_screen") }) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Spacer(modifier = Modifier.padding(padding))
        if (isMapView) {
            EventsMapView(navController, viewModel)
        } else {
            EventsListView(navController, viewModel)
        }
    }
}
