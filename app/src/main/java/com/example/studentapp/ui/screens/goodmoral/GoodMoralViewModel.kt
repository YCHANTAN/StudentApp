package com.example.studentapp.ui.screens.goodmoral

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.repository.AuthRepositoryImpl
import com.example.studentapp.domain.model.ProfileOverview
import com.example.studentapp.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class GoodMoralViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {
    var profile by mutableStateOf<ProfileOverview?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            profile = authRepository.getProfile()
            isLoading = false
        }
    }
}
