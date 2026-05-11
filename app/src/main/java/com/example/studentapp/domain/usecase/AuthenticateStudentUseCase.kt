package com.example.studentapp.domain.usecase

import com.example.studentapp.domain.repository.AuthRepository

data class AuthenticationResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null
)

class AuthenticateStudentUseCase(
    private val repository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl()
) {
    suspend operator fun invoke(studentId: String, password: String): AuthenticationResult {
        val normalizedStudentId = studentId.trim()

        if (normalizedStudentId.isEmpty()) {
            return AuthenticationResult(
                isSuccess = false,
                errorMessage = "Enter your student ID."
            )
        }

        if (password.isBlank()) {
            return AuthenticationResult(
                isSuccess = false,
                errorMessage = "Enter your password."
            )
        }

        if (!normalizedStudentId.startsWith("STU-", ignoreCase = true)) {
            return AuthenticationResult(
                isSuccess = false,
                errorMessage = "Student ID must start with 'STU-'"
            )
        }

        if (normalizedStudentId.length < 5) {
            return AuthenticationResult(
                isSuccess = false,
                errorMessage = "Use a valid student ID (at least 5 characters)."
            )
        }

        if (password.length < 6) {
            return AuthenticationResult(
                isSuccess = false,
                errorMessage = "Password must be at least 6 characters."
            )
        }

        return repository.login(normalizedStudentId, password)
    }
}
