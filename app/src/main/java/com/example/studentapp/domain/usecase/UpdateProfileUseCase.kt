package com.example.studentapp.domain.usecase

import com.example.studentapp.domain.model.ProfileOverview
import com.example.studentapp.domain.repository.AuthRepository

class UpdateProfileUseCase(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl()
) {
    suspend operator fun invoke(id: String, profile: ProfileOverview): Boolean {
        return authRepository.updateProfile(id, profile)
    }
}
