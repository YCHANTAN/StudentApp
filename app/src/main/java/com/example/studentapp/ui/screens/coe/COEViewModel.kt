package com.example.studentapp.ui.screens.coe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.repository.AuthRepositoryImpl
import com.example.studentapp.data.repository.DocumentRequestRepositoryImpl
import com.example.studentapp.domain.model.ProfileOverview
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.domain.repository.DocumentRequestRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class COEViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl(),
    private val documentRequestRepository: DocumentRequestRepository = DocumentRequestRepositoryImpl()
) : ViewModel() {
    var profile by mutableStateOf<ProfileOverview?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isSubmitting by mutableStateOf(false)
        private set

    private val _eventFlow = MutableSharedFlow<COEEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class COEEvent {
        data class ShowToast(val message: String) : COEEvent()
        object NavigateToFinance : COEEvent()
    }

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

    fun submitRequest(purpose: String) {
        viewModelScope.launch {
            val currentProfile = profile ?: return@launch
            isSubmitting = true
            val success = documentRequestRepository.createDocumentRequest(
                studentId = currentProfile.id,
                type = "COE",
                purpose = purpose
            )
            isSubmitting = false

            if (success) {
                _eventFlow.emit(COEEvent.ShowToast("COE Request submitted successfully!"))
                _eventFlow.emit(COEEvent.NavigateToFinance)
            } else {
                _eventFlow.emit(COEEvent.ShowToast("Failed to submit COE Request. Please try again."))
            }
        }
    }
}
