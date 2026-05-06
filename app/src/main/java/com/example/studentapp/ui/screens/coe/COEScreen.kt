package com.example.studentapp.ui.screens.coe

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
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.coe.components.COEHeader
import com.example.studentapp.ui.screens.coe.components.COEIntroduction
import com.example.studentapp.ui.screens.coe.components.COENoteCard
import com.example.studentapp.ui.screens.coe.components.COEQuickActions
import com.example.studentapp.ui.screens.coe.components.TermSelectionCard

@Composable
fun COEScreen(
    navigationItems: List<StudentBottomNavItem> = buildPrimaryBottomNavItems(),
    selectedNavItemId: String = "services",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            COEHeader(
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
            COEIntroduction()

            Spacer(modifier = Modifier.height(32.dp))

            TermSelectionCard()

            Spacer(modifier = Modifier.height(24.dp))

            COEQuickActions(
                onGenerateDigitalClick = { /* Handle */ },
                onRequestPrintedClick = { /* Handle */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            COENoteCard()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
