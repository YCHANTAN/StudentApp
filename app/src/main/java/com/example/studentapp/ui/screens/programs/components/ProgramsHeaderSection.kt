package com.example.studentapp.ui.screens.programs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentSearchBar
import com.example.studentapp.ui.screens.programs.models.ProgramsTab

@Composable
fun ProgramsHeaderSection(
    searchQuery: String,
    selectedTab: ProgramsTab,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onTabSelected: (ProgramsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    StudentHeader(
        title = "Programs & Prospectus",
        onBackClick = onBackClick,
        modifier = modifier,
        bottomContent = {
            StudentSearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search programs..."
            )

            ProgramsTabs(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
