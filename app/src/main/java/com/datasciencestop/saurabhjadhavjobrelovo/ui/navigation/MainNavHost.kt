package com.datasciencestop.saurabhjadhavjobrelovo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.datasciencestop.saurabhjadhavjobrelovo.ui.screens.*
import com.datasciencestop.saurabhjadhavjobrelovo.viewmodel.EventViewModel

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    // Create a shared instance of EventViewModel
    val viewModel = remember { EventViewModel() }

    NavHost(navController, startDestination = "main_screen", modifier = modifier) {
        composable("main_screen") {
            MainScreen(navController, viewModel)
        }
        composable("event_details/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailsScreen(navController, eventId, viewModel)
        }
        composable("filter_screen") {
            FilterScreen(navController, viewModel)
        }
    }
}
