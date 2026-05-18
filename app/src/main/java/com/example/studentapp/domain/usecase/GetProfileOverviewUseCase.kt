package com.example.studentapp.domain.usecase

import com.example.studentapp.domain.model.ProfileOverview
import com.example.studentapp.domain.repository.AuthRepository

class GetProfileOverviewUseCase(
    private val repository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl()
) {
    suspend operator fun invoke(): ProfileOverview {
        return repository.getProfile() ?: ProfileOverview(
            id = "N/A",
            accountId = "N/A",
            fullName = "Error Loading Profile",
            emailAddress = "N/A",
            phoneNumber = "N/A",
            accountLabel = "Student Portal Account",
            programSummary = "N/A",
            emergencyContact = com.example.studentapp.domain.model.EmergencyContactInfo(
                name = "N/A",
                relationship = "N/A",
                phoneNumber = "N/A"
            ),
            twoFactorStatus = com.example.studentapp.domain.model.TwoFactorStatus.Disabled,
            notificationPreferences = com.example.studentapp.domain.model.NotificationPreferences(
                emailNotifications = false,
                smsNotifications = false,
                systemAlerts = false
            )
        )
    }
}
