package com.example.studentapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.dashboard.components.CampusDigitalIdCard
import com.example.studentapp.ui.screens.dashboard.components.DashboardHeader
import com.example.studentapp.ui.screens.dashboard.components.RequestStatusSection
import com.example.studentapp.ui.screens.dashboard.components.StatsSection
import com.example.studentapp.ui.screens.dashboard.components.StudyLoadSection
import com.example.studentapp.ui.screens.dashboard.models.DashboardUiState
import com.example.studentapp.ui.screens.dashboard.models.buildDashboardUiState
import com.example.studentapp.ui.theme.Spacing
import com.example.studentapp.ui.theme.StudentAppTheme

import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(),
    navigationItems: List<StudentBottomNavItem> = buildPrimaryBottomNavItems(),
    selectedNavItemId: String = "home",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onViewScheduleClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val state = viewModel.state

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DashboardHeader(
                studentName = state.studentName,
                hasUnreadNotifications = true,
                onNotificationClick = onNotificationClick
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
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Spacing.Large,
                top = innerPadding.calculateTopPadding() + Spacing.Large,
                end = Spacing.Large,
                bottom = innerPadding.calculateBottomPadding() + Spacing.Large
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            item {
                StatsSection(stats = state.stats)
            }

            item {
                CampusDigitalIdCard(
                    studentName = state.studentName,
                    studentId = state.studentId,
                    status = state.idStatus
                )
            }

            item {
                StudyLoadSection(
                    courses = state.courses,
                    onViewScheduleClick = onViewScheduleClick
                )
            }

            item {
                RequestStatusSection(requestStatus = state.requestStatus)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    StudentAppTheme(dynamicColor = false) {
        DashboardScreen()
    }
}
