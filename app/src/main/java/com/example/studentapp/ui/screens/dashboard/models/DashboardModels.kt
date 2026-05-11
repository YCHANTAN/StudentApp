package com.example.studentapp.ui.screens.dashboard.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class DashboardUiState(
    val studentName: String,
    val studentId: String,
    val idStatus: String,
    val stats: List<DashboardStat>,
    val courses: List<CourseSnapshot>,
    val requestStatus: ServiceRequestStatus
)

@Immutable
data class DashboardStat(
    val value: String,
    val label: String,
    val icon: ImageVector,
    val isHighlighted: Boolean = false
)

@Immutable
data class CourseSnapshot(
    val code: String,
    val title: String,
    val schedule: String
)

@Immutable
data class ServiceRequestStatus(
    val title: String,
    val reference: String,
    val statusLabel: String,
    val progress: Float,
    val estimatedCompletion: String
)

fun buildDashboardUiState(): DashboardUiState {
    return DashboardUiState(
        studentName = "---",
        studentId = "---",
        idStatus = "---",
        stats = listOf(
            DashboardStat(
                value = "₱0.00",
                label = "Current Balance",
                icon = Icons.Filled.AccountBalanceWallet,
                isHighlighted = true
            ),
            DashboardStat(
                value = "0.00",
                label = "GPA",
                icon = Icons.Filled.Star
            ),
            DashboardStat(
                value = "0",
                label = "Units Completed",
                icon = Icons.Filled.School
            )
        ),
        courses = emptyList(),
        requestStatus = ServiceRequestStatus(
            title = "No recent requests",
            reference = "---",
            statusLabel = "---",
            progress = 0f,
            estimatedCompletion = "---"
        )
    )
}
