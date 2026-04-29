package com.example.studentapp.ui.screens.courses.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentHeaderIconButton
import com.example.studentapp.ui.components.StudentSearchBar
import com.example.studentapp.ui.screens.courses.models.CourseStatus

@Composable
fun CoursesHeaderSection(
    searchQuery: String,
    selectedStatus: CourseStatus,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onStatusSelected: (CourseStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    StudentHeader(
        title = "My Courses",
        onBackClick = onBackClick,
        modifier = modifier,
        actions = {
            StudentHeaderIconButton(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                onClick = {}
            )
        },
        bottomContent = {
            StudentSearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search courses..."
            )

            CoursesTabs(
                selectedStatus = selectedStatus,
                onStatusSelected = onStatusSelected
            )
        }
    )
}
