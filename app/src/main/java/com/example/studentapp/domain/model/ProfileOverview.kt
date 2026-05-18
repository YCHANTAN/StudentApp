package com.example.studentapp.domain.model

enum class TwoFactorStatus {
    Disabled,
    PendingVerification,
    Enabled
}

data class EmergencyContactInfo(
    val name: String,
    val relationship: String,
    val phoneNumber: String
)

data class NotificationPreferences(
    val emailNotifications: Boolean,
    val smsNotifications: Boolean,
    val systemAlerts: Boolean
)

data class ProfileOverview(
    val id: String,
    val accountId: String,
    val fullName: String,
    val emailAddress: String,
    val phoneNumber: String,
    val accountLabel: String,
    val programSummary: String,
    val emergencyContact: EmergencyContactInfo,
    val twoFactorStatus: TwoFactorStatus,
    val notificationPreferences: NotificationPreferences
)
