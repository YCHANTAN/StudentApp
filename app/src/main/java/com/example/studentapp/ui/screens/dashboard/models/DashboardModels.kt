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
        studentName = "Christian Osorno",
        studentId = "STU-2024-1",
        idStatus = "Active",
        stats = listOf(
            DashboardStat(
                value = "₱1,050.00",
                label = "Current Balance",
                icon = Icons.Filled.AccountBalanceWallet,
                isHighlighted = true
            ),
            DashboardStat(
                value = "1.25",
                label = "GPA",
                icon = Icons.Filled.Star
            ),
            DashboardStat(
                value = "15",
                label = "Units Completed",
                icon = Icons.Filled.School
            )
        ),
        courses = listOf(
            CourseSnapshot(
                code = "CS301",
                title = "Advanced Algorithms",
                schedule = "Mon/Wed | 10:00 AM — 11:30 AM"
            ),
            CourseSnapshot(
                code = "MATH402",
                title = "Stochastic Processes",
                schedule = "Tue/Thu | 01:00 PM — 02:30 PM"
            )
        ),
        requestStatus = ServiceRequestStatus(
            title = "Request for TOR",
            reference = "REQ-TOR-001",
            statusLabel = "READY_FOR_PICKUP",
            progress = 0.9f,
            estimatedCompletion = "Ready for Pickup"
        )
    )
}
