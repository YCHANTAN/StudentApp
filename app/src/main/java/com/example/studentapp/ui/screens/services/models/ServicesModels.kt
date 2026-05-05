package com.example.studentapp.ui.screens.services.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DocumentRequestItem(
    val id: String,
    val type: String,
    val status: String,
    val date: String,
    val reference: String
)

data class DocumentRequest(
    val title: String,
    val subtitle: String,
    val status: DocumentRequestStatus,
    val icon: ImageVector,
    val iconTint: Color
)

enum class DocumentRequestStatus {
    PROCESSING,
    ACCEPTED,
    READY_FOR_PICKUP
}

data class DocumentType(
    val label: String,
    val icon: ImageVector,
    val iconTint: Color
)

data class LibraryLink(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

data class Complaint(
    val title: String,
    val status: ComplaintStatus
)

enum class ComplaintStatus(val label: String, val color: Color) {
    IN_REVIEW("IN REVIEW", Color(0xFF1F5C23).copy(alpha = 0.6f)),
    RESOLVED("RESOLVED", Color(0xFF16A34A))
}
