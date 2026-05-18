package com.example.studentapp.ui.screens.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentNotificationButton
import com.example.studentapp.ui.screens.library.models.LibraryTab

@Composable
fun LibraryHeaderSection(
    selectedTab: LibraryTab,
    onTabSelected: (LibraryTab) -> Unit,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    StudentHeader(
        title = "Campus Library",
        onBackClick = onBackClick,
        modifier = modifier,
        actions = {
            StudentNotificationButton(
                onClick = onNotificationClick
            )
        },
        bottomContent = {
            LibraryTabSwitcher(
                tabs = LibraryTab.entries,
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        }
    )
}

@Composable
private fun LibraryTabSwitcher(
    tabs: List<LibraryTab>,
    selectedTab: LibraryTab,
    onTabSelected: (LibraryTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        tabs.forEach { tab ->
            LibraryTabItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LibraryTabItem(
    tab: LibraryTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val activeColor = MaterialTheme.colorScheme.primary

        Text(
            text = tab.label,
            modifier = Modifier.padding(top = 16.dp, bottom = 13.dp),
            color = if (isSelected) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 0.21.sp
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isSelected) activeColor else Color.Transparent
                )
                .padding(vertical = 1.5.dp)
        )
    }
}
