package com.datasciencestop.saurabhjadhavjobrelovo.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.datasciencestop.saurabhjadhavjobrelovo.viewmodel.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController, viewModel: EventViewModel) {
    val context = LocalContext.current
    val categories = listOf("All", "Music", "Food", "Art", "Sports", "Technology")

    var selectedCategory by remember { mutableStateOf(viewModel.selectedCategory) }
    var startDate by remember { mutableStateOf(viewModel.startDate) }
    var endDate by remember { mutableStateOf(viewModel.endDate) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Filter Events") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Category Dropdown
            Text(text = "Category", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = viewModel.isCategoryDropdownExpanded,
                onExpandedChange = { viewModel.isCategoryDropdownExpanded = !viewModel.isCategoryDropdownExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedCategory,
                    onValueChange = {},
                    label = { Text("Select Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = viewModel.isCategoryDropdownExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = viewModel.isCategoryDropdownExpanded,
                    onDismissRequest = { viewModel.isCategoryDropdownExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                viewModel.isCategoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date Range Inputs
            val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

            Text(text = "Start Date", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            DatePickerField(
                label = "Select Start Date",
                selectedDate = startDate,
                onDateSelected = { date -> startDate = date }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "End Date", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            DatePickerField(
                label = "Select End Date",
                selectedDate = endDate,
                onDateSelected = { date -> endDate = date }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Apply filters in viewModel
                    viewModel.applyFilters(selectedCategory, startDate, endDate)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filters")
            }
        }
    }
}

@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Parse selectedDate to set initial date in DatePickerDialog
    if (selectedDate.isNotEmpty()) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val date = dateFormatter.parse(selectedDate)
            date?.let {
                calendar.time = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(selectedCal.time)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select Date"
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
