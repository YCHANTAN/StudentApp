package com.example.studentapp.ui.screens.academic.models

import com.example.studentapp.domain.model.AcademicOverview

data class AcademicUiState(
    val studentName: String,
    val studentId: String = "",
    val programSummary: String,
    val yearLevel: String = "",
    val currentTerm: String = "2nd Semester, 2024-2025",
    val services: List<AcademicServiceCardUiModel>,
    val supportCard: AcademicSupportUiModel
)

data class AcademicServiceCardUiModel(
    val code: String,
    val title: String,
    val description: String
)

data class AcademicSupportUiModel(
    val title: String,
    val description: String,
    val actionLabel: String
)

fun AcademicOverview.toUiState(): AcademicUiState {
    return AcademicUiState(
        studentName = studentName,
        programSummary = "$programName • $yearLevel",
        services = services.map { service ->
            AcademicServiceCardUiModel(
                code = service.code,
                title = service.title,
                description = service.description
            )
        },
        supportCard = AcademicSupportUiModel(
            title = supportMessage.title,
            description = supportMessage.description,
            actionLabel = supportMessage.actionLabel
        )
    )
}
