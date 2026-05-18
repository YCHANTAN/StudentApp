package com.example.studentapp.ui.screens.evaluations.models

data class EvaluationCourseItem(
    val id: String,
    val codeTitle: String,
    val instructor: String,
    val iconType: EvaluationCourseIconType,
    val isExpanded: Boolean = false,
    val teachingQuality: Int = 0,
    val courseMaterials: Int = 0,
    val punctuality: Int = 0,
    val comments: String = "",
    val isSubmitting: Boolean = false
)

enum class EvaluationCourseIconType {
    DOCUMENT,
    CHART
}
