package com.example.studentapp.ui.screens.services.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.ui.graphics.Color

val sampleDocumentTypes = listOf(
    DocumentType(
        label = "TOR",
        icon = Icons.Default.Description,
        iconTint = Color(0xFFD4AF37)
    ),
    DocumentType(
        label = "Good Moral",
        icon = Icons.Default.VerifiedUser,
        iconTint = Color(0xFFD4AF37)
    ),
    DocumentType(
        label = "COE",
        icon = Icons.Default.CardMembership,
        iconTint = Color(0xFFD4AF37)
    )
)

val sampleLibraryLinks = listOf(
    LibraryLink(
        title = "Availability Tracker",
        subtitle = "Check physical & digital stocks",
        icon = Icons.Default.Search
    ),
    LibraryLink(
        title = "Borrowing History",
        subtitle = "View past and current loans",
        icon = Icons.Default.History
    )
)
