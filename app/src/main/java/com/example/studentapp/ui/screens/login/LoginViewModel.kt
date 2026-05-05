package com.example.studentapp.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.usecase.AuthenticateStudentUseCase
import com.example.studentapp.domain.usecase.AuthenticationResult
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authenticateStudentUseCase: AuthenticateStudentUseCase = AuthenticateStudentUseCase()
) : ViewModel() {
    var studentId by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var keepLoggedIn by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = authenticateStudentUseCase(studentId, password)
            isLoading = false
            
            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage = result.errorMessage
            }
        }
    }
}
