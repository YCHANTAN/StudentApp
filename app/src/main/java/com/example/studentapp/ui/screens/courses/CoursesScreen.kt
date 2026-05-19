package com.example.studentapp.ui.screens.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentSkeletonScaffold
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.courses.components.CourseCard
import com.example.studentapp.ui.screens.courses.components.CoursesHeaderSection
import com.example.studentapp.ui.screens.courses.models.CourseEntry
import com.example.studentapp.ui.screens.courses.models.CourseStatus
import com.example.studentapp.ui.screens.courses.models.filterCourseEntries
import com.example.studentapp.ui.theme.StudentAppTheme

@Composable
fun CoursesScreen(
    navigationItems: List<StudentBottomNavItem>,
    selectedNavItemId: String,
    onBottomNavSelected: (StudentBottomNavItem) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CoursesViewModel = viewModel()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedStatus by rememberSaveable { mutableStateOf(CourseStatus.Enrolled) }
    
    val courses = viewModel.allCourses
    val isLoading = viewModel.isLoading
    
    val filteredCourses = remember(searchQuery, selectedStatus, courses) {
        filterCourseEntries(
            courses = courses,
            searchQuery = searchQuery,
            selectedStatus = selectedStatus
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CoursesHeaderSection(
                searchQuery = searchQuery,
                selectedStatus = selectedStatus,
                onBackClick = onBackClick,
                onSearchQueryChange = { searchQuery = it },
                onStatusSelected = { selectedStatus = it }
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
        if (isLoading && courses.isEmpty()) {
            StudentSkeletonScaffold(contentPadding = innerPadding)
        } else {
            CoursesContent(
                courses = filteredCourses,
                semesterTitle = filteredCourses.firstOrNull()?.semesterTitle ?: "No Courses Found",
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
            )
        }
    }
}

@Composable
fun CoursesContent(
    courses: List<CourseEntry>,
    semesterTitle: String,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        item {
            Text(
                text = semesterTitle,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.27).sp
            )
        }

        items(
            items = courses,
            key = { "${it.code}_${it.status.name}" }
        ) { course ->
            CourseCard(
                course = course,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoursesScreenPreview() {
    StudentAppTheme(dynamicColor = false) {
        CoursesScreen(
            navigationItems = buildPrimaryBottomNavItems(),
            selectedNavItemId = "academic",
            onBottomNavSelected = {},
            onBackClick = {}
        )
    }
}
