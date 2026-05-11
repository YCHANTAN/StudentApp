package com.example.studentapp.ui.screens.adjustment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.screens.adjustment.components.*
import com.example.studentapp.ui.screens.adjustment.models.AdjustmentCourseIconType
import com.example.studentapp.ui.screens.adjustment.models.AdjustmentCourseItem
import com.example.studentapp.ui.theme.Spacing

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.Color

@Composable
fun AdjustmentScreen(
    navigationItems: List<StudentBottomNavItem> = emptyList(),
    selectedNavItemId: String = "",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    viewModel: AdjustmentViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadEnrollment("user_123") // Using the seeded student ID
    }

    if (uiState.showRemoveDialog != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelRemove() },
            title = { Text("Remove Course") },
            text = { Text("Are you sure you want to remove this enrolled course?") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmRemove() }) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelRemove() }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState.showScheduleDialog != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelScheduleChange() },
            title = { Text("Change Schedule") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select a new schedule for ${uiState.showScheduleDialog.title}")
                    HorizontalDivider()
                    if (uiState.alternativeCourses.isEmpty()) {
                        Text("No other schedules available.", fontStyle = FontStyle.Italic)
                    } else {
                        for (alt in uiState.alternativeCourses) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectNewSchedule(alt) }
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(alt.scheduleAndUnits, fontWeight = FontWeight.Bold)
                                Text(alt.professor, style = MaterialTheme.typography.bodySmall)
                            }
                            HorizontalDivider()
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.cancelScheduleChange() }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AdjustmentTopBar(
                title = "Course Adjustment",
                semesterLabel = "SPRING 2024",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            StudentBottomNavBar(
                items = navigationItems,
                selectedItemId = selectedNavItemId,
                onItemSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(Spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                item {
                    AdjustmentLoadCard(
                        currentLoad = uiState.totalUnits,
                        maxLoad = 21
                    )
                }

                item {
                    AdjustmentSectionHeader(
                        title = "Currently Enrolled"
                    )
                }

                items(uiState.enrolledCourses) { course ->
                    AdjustmentCourseCard(
                        item = course,
                        onChangeScheduleClick = { viewModel.requestChangeSchedule(course) },
                        onRemoveClick = { viewModel.requestRemove(course) }
                    )
                }

                item {
                    AdjustmentSectionHeader(
                        title = "Add New Course",
                        addMode = true
                    )
                }

                item {
                    AdjustmentSearchBar(
                        query = searchQuery,
                        onQueryChange = { 
                            searchQuery = it
                            viewModel.searchCourses(it)
                        }
                    )
                }

                items(uiState.searchResults) { course ->
                    AdjustmentCourseCard(
                        item = course,
                        isAddMode = true,
                        onAddClick = { 
                            viewModel.addCourse(course)
                            searchQuery = ""
                        }
                    )
                }

                item {
                    AdjustmentSaveButton(
                        text = "Save Changes",
                        onClick = {
                            viewModel.saveChanges()
                            onSaveClick()
                        }
                    )
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
