package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.LoginRequest
import com.example.studentapp.data.remote.NetworkModule
import com.example.studentapp.domain.model.EmergencyContactInfo
import com.example.studentapp.domain.model.NotificationPreferences
import com.example.studentapp.domain.model.ProfileOverview
import com.example.studentapp.domain.model.TwoFactorStatus
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.domain.usecase.AuthenticationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(studentId: String, password: String): AuthenticationResult = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.authApi.login(LoginRequest(studentId, password))
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    val loginData = apiResponse.data
                    NetworkModule.setAuthToken(loginData.accessToken)
                    AuthenticationResult(isSuccess = true)
                } else {
                    AuthenticationResult(isSuccess = false, errorMessage = "Login failed: Server returned success=false")
                }
            } else {
                AuthenticationResult(isSuccess = false, errorMessage = "Invalid credentials or server error")
            }
        } catch (e: Exception) {
            AuthenticationResult(isSuccess = false, errorMessage = "Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getProfile(): ProfileOverview? = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.authApi.getMe()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    val user = apiResponse.data
                    ProfileOverview(
                        id = user.id,
                        accountId = user.studentId,
                        fullName = "${user.firstName} ${user.lastName}",
                        emailAddress = user.email,
                        phoneNumber = user.phoneNumber ?: "Not Set",
                        accountLabel = user.accountLabel ?: "Student Portal Account",
                        programSummary = user.programSummary ?: "${user.program ?: "N/A"} • Year ${user.yearLevel ?: "N/A"}",
                        emergencyContact = EmergencyContactInfo(
                            name = user.emergencyContact?.name ?: "Not Set",
                            relationship = user.emergencyContact?.relationship ?: "N/A",
                            phoneNumber = user.emergencyContact?.phoneNumber ?: "N/A"
                        ),
                        twoFactorStatus = TwoFactorStatus.Disabled,
                        notificationPreferences = NotificationPreferences(
                            emailNotifications = user.notifications?.email ?: true,
                            smsNotifications = user.notifications?.sms ?: false,
                            systemAlerts = user.notifications?.system ?: true
                        )
                    )
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun logout() {
        NetworkModule.setAuthToken(null)
    }

    override suspend fun isLoggedIn(): Boolean {
        return NetworkModule.getAuthToken() != null
    }
}
