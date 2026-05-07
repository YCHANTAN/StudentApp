package com.example.studentapp.ui.screens.academic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.domain.usecase.GetAcademicOverviewUseCase
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.academic.components.AcademicDashboardMenuCard
import com.example.studentapp.ui.screens.academic.components.AcademicDashboardSectionHeader
import com.example.studentapp.ui.screens.academic.components.AcademicHeaderSection
import com.example.studentapp.ui.screens.academic.components.AcademicHeroCard
import com.example.studentapp.ui.screens.academic.components.AcademicSupportSection
import com.example.studentapp.ui.screens.academic.models.*
import com.example.studentapp.ui.theme.Spacing
import com.example.studentapp.ui.theme.StudentAppTheme

@Composable
fun AcademicScreen(
    navigationItems: List<StudentBottomNavItem>,
    selectedNavItemId: String,
    onBottomNavSelected: (StudentBottomNavItem) -> Unit,
    onBackClick: () -> Unit,
    onViewAllClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onCoursesClick: () -> Unit = {},
    onEnrollmentClick: () -> Unit = {},
    onProgramsClick: () -> Unit = {},
    onGradesClick: () -> Unit = {},
    onEvaluationClick: () -> Unit = {},
    onStudyLoadClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: AcademicViewModel = viewModel()
    
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    
    AcademicServicesScreen(
        state = viewModel.uiState,
        navigationItems = navigationItems,
        selectedNavItemId = selectedNavItemId,
        onBottomNavSelected = onBottomNavSelected,
        onBackClick = onBackClick,
        onViewAllClick = onViewAllClick,
        onContactSupportClick = onContactSupportClick,
        onCoursesClick = onCoursesClick,
        onEnrollmentClick = onEnrollmentClick,
        onProgramsClick = onProgramsClick,
        onGradesClick = onGradesClick,
        onEvaluationClick = onEvaluationClick,
        onStudyLoadClick = onStudyLoadClick,
        onNotificationClick = onNotificationClick,
        modifier = modifier
    )
}

@Composable
fun AcademicServicesScreen(
    state: AcademicUiState,
    navigationItems: List<StudentBottomNavItem>,
    selectedNavItemId: String,
    onBottomNavSelected: (StudentBottomNavItem) -> Unit,
    onBackClick: () -> Unit,
    onViewAllClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onCoursesClick: () -> Unit,
    onEnrollmentClick: () -> Unit,
    onProgramsClick: () -> Unit,
    onGradesClick: () -> Unit,
    onEvaluationClick: () -> Unit,
    onStudyLoadClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dashboardItems = remember { buildAcademicDashboardMenuItems() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AcademicHeaderSection(
                onBackClick = onBackClick,
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
        AcademicServicesContent(
            state = state,
            dashboardItems = dashboardItems,
            contentPadding = PaddingValues(
                start = Spacing.Medium,
                top = innerPadding.calculateTopPadding() + Spacing.Large,
                end = Spacing.Medium,
                bottom = innerPadding.calculateBottomPadding() + Spacing.Large
            ),
            onViewAllClick = onViewAllClick,
            onContactSupportClick = onContactSupportClick,
            onDashboardItemClick = { item ->
                when (item.id) {
                    ACADEMIC_MENU_PROGRAMS -> onProgramsClick()
                    ACADEMIC_MENU_COURSES -> onCoursesClick()
                    ACADEMIC_MENU_ENROLLMENT -> onEnrollmentClick()
                    ACADEMIC_MENU_GRADES -> onGradesClick()
                    ACADEMIC_MENU_EVALUATION -> onEvaluationClick()
                    ACADEMIC_MENU_STUDY_LOAD -> onStudyLoadClick()
                }
            }
        )
    }
}

@Composable
fun AcademicServicesContent(
    state: AcademicUiState,
    dashboardItems: List<AcademicDashboardMenuItem>,
    contentPadding: PaddingValues,
    onViewAllClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onDashboardItemClick: (AcademicDashboardMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            AcademicHeroCard(
                studentName = state.studentName,
                studentId = state.studentId,
                program = state.programSummary,
                yearLevel = state.yearLevel,
                currentTerm = state.currentTerm
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            AcademicDashboardSectionHeader(onViewAllClick = onViewAllClick)
        }

        items(dashboardItems) { item ->
            AcademicDashboardMenuCard(
                item = item,
                onClick = { onDashboardItemClick(item) }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            AcademicSupportSection(onContactSupportClick = onContactSupportClick)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AcademicScreenPreview() {
    StudentAppTheme(dynamicColor = false) {
        AcademicServicesScreen(
            state = GetAcademicOverviewUseCase().invoke().toUiState(),
            navigationItems = buildPrimaryBottomNavItems(),
            selectedNavItemId = "academic",
            onBottomNavSelected = {},
            onBackClick = {},
            onViewAllClick = {},
            onContactSupportClick = {},
            onCoursesClick = {},
            onEnrollmentClick = {},
            onProgramsClick = {},
            onGradesClick = {},
            onEvaluationClick = {},
            onStudyLoadClick = {},
            onNotificationClick = {}
        )
    }
}
