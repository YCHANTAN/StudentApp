package com.example.studentapp.ui.screens.tor

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

class TORViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl(),
    private val documentRequestRepository: DocumentRequestRepository = DocumentRequestRepositoryImpl()
) : ViewModel() {
    var profile by mutableStateOf<ProfileOverview?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isSubmitting by mutableStateOf(false)
        private set

    private val _eventFlow = MutableSharedFlow<TOREvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class TOREvent {
        data class ShowToast(val message: String) : TOREvent()
        object NavigateToFinance : TOREvent()
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

    fun submitRequest(copies: Int, purpose: String) {
        viewModelScope.launch {
            val currentProfile = profile ?: return@launch
            isSubmitting = true
            val success = documentRequestRepository.createDocumentRequest(
                studentId = currentProfile.id,
                type = "TOR",
                purpose = purpose,
                copies = copies
            )
            isSubmitting = false

            if (success) {
                _eventFlow.emit(TOREvent.ShowToast("TOR Request submitted successfully!"))
                _eventFlow.emit(TOREvent.NavigateToFinance)
            } else {
                _eventFlow.emit(TOREvent.ShowToast("Failed to submit TOR Request. Please try again."))
            }
        }
    }
}
