package com.example.studentapp.ui.screens.tor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.tor.components.RequestDetailsSection
import com.example.studentapp.ui.screens.tor.components.StudentProfileHeader
import com.example.studentapp.ui.screens.tor.components.TORHeader

@Composable
fun TORScreen(
    navigationItems: List<StudentBottomNavItem> = buildPrimaryBottomNavItems(),
    selectedNavItemId: String = "services",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    viewModel: TORViewModel = viewModel()
) {
    val profile = viewModel.profile

    Scaffold(
        topBar = {
            TORHeader(
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            StudentProfileHeader(
                name = profile?.fullName ?: "Loading...",
                studentId = profile?.accountId ?: "---",
                courseYear = profile?.programSummary ?: "---",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBhyatqy3NA5y0usr0T-RBR7iZOzY-kOaNvtGrCIc_bu1CNa2dp4KqfXHZMwl1R_YHFz9nNPwWie304Z_ZcSvRuFWCsLPt1OnQxsBEpyWflteLmsTUd6-oZE4mc6bF7riUG_3Ko1FYzUt7vRL3zK1vWzWbFmoBz9FwRT9kx1rekJVoKFsXlUsTh5wyRiWtgDS3hk2AmPIvuPwncJHenOu73pq97E2O3vIO3ph9cLM5BBmjDxza6SwLbyAlwUpxaFwWaeVUHkbQjZh0"
            )

            RequestDetailsSection()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
