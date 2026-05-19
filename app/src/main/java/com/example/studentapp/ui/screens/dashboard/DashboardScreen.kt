package com.example.studentapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.StudentHeaderSkeleton
import com.example.studentapp.ui.components.StudentSkeletonBlock
import com.example.studentapp.ui.components.StudentSkeletonCard
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.dashboard.components.CampusDigitalIdCard
import com.example.studentapp.ui.screens.dashboard.components.DashboardHeader
import com.example.studentapp.ui.screens.dashboard.components.RequestStatusSection
import com.example.studentapp.ui.screens.dashboard.components.StatsSection
import com.example.studentapp.ui.screens.dashboard.components.StudyLoadSection
import com.example.studentapp.ui.screens.dashboard.models.CourseSnapshot
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
    onFinanceClick: () -> Unit = {},
    onGradesClick: () -> Unit = {},
    onCoursesClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onCourseClick: (CourseSnapshot) -> Unit = {},
    onRequestStatusClick: () -> Unit = {}
) {
    val state = viewModel.state
    val isInitialLoading = viewModel.isLoading && state.studentName == "---"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (isInitialLoading) {
                StudentHeaderSkeleton()
            } else {
                DashboardHeader(
                    studentName = state.studentName,
                    hasUnreadNotifications = true,
                    onNotificationClick = onNotificationClick
                )
            }
        },
        bottomBar = {
            StudentBottomNavBar(
                items = navigationItems,
                selectedItemId = selectedNavItemId,
                onItemSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        if (isInitialLoading) {
            DashboardSkeletonContent(
                contentPadding = PaddingValues(
                    start = Spacing.Large,
                    top = innerPadding.calculateTopPadding() + Spacing.Large,
                    end = Spacing.Large,
                    bottom = innerPadding.calculateBottomPadding() + Spacing.Large
                )
            )
        } else {
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
                    StatsSection(
                        stats = state.stats,
                        onStatClick = { stat ->
                            when (stat.label) {
                                "Current Balance" -> onFinanceClick()
                                "GPA" -> onGradesClick()
                                "Units Completed" -> onCoursesClick()
                            }
                        }
                    )
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
                        onViewScheduleClick = onViewScheduleClick,
                        onCourseClick = onCourseClick
                    )
                }

                item {
                    RequestStatusSection(
                        requestStatus = state.requestStatus,
                        onClick = onRequestStatusClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardSkeletonContent(
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {
        item {
            StudentSkeletonBlock(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(136.dp),
                radius = com.example.studentapp.ui.theme.Radius.Large
            )
        }

        item {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                StudentSkeletonBlock(
                    modifier = Modifier
                        .weight(1f)
                        .height(136.dp),
                    radius = com.example.studentapp.ui.theme.Radius.Large
                )
                StudentSkeletonBlock(
                    modifier = Modifier
                        .weight(1f)
                        .height(136.dp),
                    radius = com.example.studentapp.ui.theme.Radius.Large
                )
            }
        }

        item {
            StudentSkeletonBlock(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(360.dp),
                radius = com.example.studentapp.ui.theme.Radius.Large
            )
        }

        items(2) {
            StudentSkeletonCard()
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
