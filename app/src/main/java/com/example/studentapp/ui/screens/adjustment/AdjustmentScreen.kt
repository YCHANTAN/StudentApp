package com.example.studentapp.ui.screens.adjustment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.screens.adjustment.components.*
import com.example.studentapp.ui.screens.adjustment.models.AdjustmentCourseIconType
import com.example.studentapp.ui.screens.adjustment.models.AdjustmentCourseItem
import com.example.studentapp.ui.theme.Spacing

@Composable
@Preview
fun AdjustmentScreen(
    navigationItems: List<StudentBottomNavItem> = emptyList(),
    selectedNavItemId: String = "",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onChangeScheduleClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val enrolledCourses = remember {
        listOf(
            AdjustmentCourseItem(
                title = "CS101: Computer Science",
                scheduleAndUnits = "Mon/Wed 10:00 AM • 3 Units",
                professor = "Prof. Sarah Smith",
                iconType = AdjustmentCourseIconType.MONITOR
            ),
            AdjustmentCourseItem(
                title = "MATH202: Calculus II",
                scheduleAndUnits = "Tue/Thu 2:00 PM • 4 Units",
                professor = "Prof. Robert Johnson",
                iconType = AdjustmentCourseIconType.GRID
            )
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            item {
                AdjustmentLoadCard(
                    currentLoad = 15,
                    maxLoad = 21
                )
            }

            item {
                AdjustmentSectionHeader(
                    title = "Currently Enrolled"
                )
            }

            items(enrolledCourses) { course ->
                AdjustmentCourseCard(
                    item = course,
                    onChangeScheduleClick = onChangeScheduleClick,
                    onRemoveClick = {}
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
                    onQueryChange = { searchQuery = it }
                )
            }

            item {
                AdjustmentSaveButton(
                    text = "Save Changes",
                    onClick = onSaveClick
                )
            }
        }
    }
}
