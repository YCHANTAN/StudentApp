package com.example.studentapp.ui.screens.grades.models

enum class SubjectStatus {
    COMPLETED,
    IN_PROGRESS
}

enum class SubjectIconType {
    CODE,
    DATABASE,
    AI,
    DESIGN
}

data class GradesSubjectItem(
    val title: String,
    val codeCredits: String,
    val gradePoint: String,
    val status: SubjectStatus,
    val iconType: SubjectIconType,
    val semester: String,
    val remarks: String? = null
)
