 package com.example.studentapp.ui.screens.schedule.models

import androidx.compose.runtime.Immutable

@Immutable
data class ScheduleUiState(
    val studentName: String,
    val sections: List<ScheduleDaySection>
)

@Immutable
data class ScheduleDaySection(
    val dayLabel: String,
    val entries: List<ScheduleEntry>
)

@Immutable
data class ScheduleEntry(
    val courseCode: String,
    val courseTitle: String,
    val timeRange: String,
    val room: String,
    val instructor: String
)

fun buildScheduleUiState(): ScheduleUiState {
    return ScheduleUiState(
        studentName = "Loading...",
        sections = emptyList()
    )
}
