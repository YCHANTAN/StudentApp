package com.example.studentapp.ui.screens.academic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.repository.AuthRepositoryImpl
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.ui.screens.academic.models.AcademicSupportUiModel
import com.example.studentapp.ui.screens.academic.models.AcademicUiState
import kotlinx.coroutines.launch

class AcademicViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepositoryImpl()

    var uiState by mutableStateOf(
        AcademicUiState(
            studentName = "Loading...",
            programSummary = "Loading...",
            services = emptyList(),
            supportCard = AcademicSupportUiModel(
                title = "Academic Support",
                description = "Get help with your studies",
                actionLabel = "Contact Support"
            )
        )
    )
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadProfile() {
        viewModelScope.launch {
            try {
                isLoading = true
                val profile = authRepository.getProfile()
                if (profile != null) {
                    val summary = profile.programSummary
                    val hasSeparator = summary.contains(" • ")
                    
                    uiState = uiState.copy(
                        studentName = profile.fullName,
                        studentId = profile.accountId,
                        programSummary = if (hasSeparator) summary.substringBefore(" • ") else summary,
                        yearLevel = if (hasSeparator) summary.substringAfter(" • ") else ""
                    )
                }
            } catch (e: Exception) {
                // Keep default state or log
            } finally {
                isLoading = false
            }
        }
    }
}
