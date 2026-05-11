package com.example.studentapp.domain.repository

import com.example.studentapp.domain.model.ProfileOverview
import com.example.studentapp.domain.usecase.AuthenticationResult

interface AuthRepository {
    suspend fun login(studentId: String, password: String): AuthenticationResult
    suspend fun getProfile(): ProfileOverview?
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}
