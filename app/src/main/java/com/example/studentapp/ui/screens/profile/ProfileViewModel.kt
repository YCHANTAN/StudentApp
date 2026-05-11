package com.example.studentapp.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.ui.screens.profile.models.ProfileUiState
import com.example.studentapp.ui.screens.profile.models.toUiState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl()
) : ViewModel() {
    var state by mutableStateOf<ProfileUiState?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                state = profile.toUiState()
            }
            isLoading = false
        }
    }
}
