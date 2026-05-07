package com.example.studentapp.ui.screens.adjustment.models

enum class AdjustmentCourseIconType {
    MONITOR,
    GRID
}

data class AdjustmentCourseItem(
    val id: String,
    val title: String,
    val scheduleAndUnits: String,
    val professor: String,
    val iconType: AdjustmentCourseIconType
)
