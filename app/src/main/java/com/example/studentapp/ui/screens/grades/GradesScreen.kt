@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.studentapp.ui.screens.grades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.screens.grades.components.GradesFilterChipRow
import com.example.studentapp.ui.screens.grades.components.GradesHeaderSummaryCard
import com.example.studentapp.ui.screens.grades.components.SubjectGradeCard
import com.example.studentapp.ui.screens.grades.models.GradeSemesterFilter
import com.example.studentapp.ui.screens.grades.models.GradesSubjectItem
import com.example.studentapp.ui.screens.grades.models.SubjectIconType
import com.example.studentapp.ui.screens.grades.models.SubjectStatus

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment

@Composable
@Preview
fun GradesScreen(
    navigationItems: List<StudentBottomNavItem> = emptyList(),
    selectedNavItemId: String = "",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
    viewModel: GradesViewModel = viewModel()
) {
    val subjects = viewModel.filteredSubjects
    val isLoading = viewModel.isLoading
    val gpa = viewModel.cumulativeGpa
    val academicLevel = viewModel.academicLevel
    val semesters = viewModel.semesters
    val selectedSemester = viewModel.selectedSemester

    val filters = semesters.map { 
        GradeSemesterFilter(it, it == selectedSemester)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Grades",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            StudentBottomNavBar(
                items = navigationItems,
                selectedItemId = selectedNavItemId,
                onItemSelected = onBottomNavSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onDownloadClick,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Outlined.FileDownload,
                    contentDescription = "Download Grades",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    ) { innerPadding ->
        if (isLoading && subjects.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    GradesHeaderSummaryCard(
                        gpa = gpa,
                        academicLevel = academicLevel
                    )
                }

                item {
                    GradesFilterChipRow(
                        filters = filters,
                        onFilterClick = { clicked ->
                            viewModel.selectSemester(clicked)
                        }
                    )
                }

                item {
                    Text(
                        text = if (subjects.isNotEmpty()) "ENROLLED SUBJECTS" else "NO SUBJECTS FOUND",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(subjects) { subject ->
                    SubjectGradeCard(item = subject)
                }
            }
        }
    }
}
