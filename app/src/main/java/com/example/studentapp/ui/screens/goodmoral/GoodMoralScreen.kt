package com.example.studentapp.ui.screens.goodmoral

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.goodmoral.components.GoodMoralHeader
import com.example.studentapp.ui.screens.goodmoral.components.NewRequestSection
import com.example.studentapp.ui.screens.goodmoral.components.RequestHistorySection

@Composable
fun GoodMoralScreen(
    navigationItems: List<StudentBottomNavItem> = buildPrimaryBottomNavItems(),
    selectedNavItemId: String = "services",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    viewModel: GoodMoralViewModel = viewModel()
) {
    val profile = viewModel.profile

    Scaffold(
        modifier = Modifier.statusBarsPadding(), 
        topBar = {
            GoodMoralHeader(
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // New Request Section
            NewRequestSection(
                studentId = profile?.accountId ?: "---",
                fullName = profile?.fullName ?: "Loading..."
            )

            // Request History Section
            RequestHistorySection()

            // Info Card
            InfoCard()
        }
    }
}

@Composable
private fun InfoCard() {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient, RoundedCornerShape(20.dp))
            .padding(24.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Column {
                Text(
                    text = "Processing Time",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Requests are typically processed within 3-5 working days. You will receive a notification once your digital certificate is ready for download.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
