package com.example.studentapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("password") val password: String,
    @SerializedName("keepLoggedIn") val keepLoggedIn: Boolean = false
)

data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("expiresIn") val expiresIn: String,
    @SerializedName("refreshToken") val refreshToken: String? = null
)

data class UserProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("program") val program: String? = null,
    @SerializedName("yearLevel") val yearLevel: Int? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("accountLabel") val accountLabel: String? = null,
    @SerializedName("programSummary") val programSummary: String? = null,
    @SerializedName("emergencyContact") val emergencyContact: EmergencyContactResponse? = null,
    @SerializedName("notifications") val notifications: NotificationSettingsResponse? = null
)

data class EmergencyContactResponse(
    @SerializedName("name") val name: String,
    @SerializedName("relationship") val relationship: String,
    @SerializedName("phoneNumber") val phoneNumber: String
)

data class NotificationSettingsResponse(
    @SerializedName("email") val email: Boolean,
    @SerializedName("sms") val sms: Boolean,
    @SerializedName("system") val system: Boolean
)

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @GET("auth/me")
    suspend fun getMe(): Response<ApiResponse<UserProfileResponse>>
}
