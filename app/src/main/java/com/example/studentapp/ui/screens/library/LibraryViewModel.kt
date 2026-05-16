package com.example.studentapp.ui.screens.library

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.remote.NetworkModule
import com.example.studentapp.data.repository.LibraryRepository
import com.example.studentapp.data.remote.ApiResponse
import com.example.studentapp.data.repository.AuthRepositoryImpl
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.ui.screens.library.models.LibraryBook
import com.example.studentapp.ui.screens.library.models.LibraryTab
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class LibraryViewModel(
    private val repository: LibraryRepository = LibraryRepository(NetworkModule.libraryApi),
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {
    private val gson = Gson()

    var booksState by mutableStateOf<List<LibraryBook>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.getLibraryBooks()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        booksState = apiResponse.data
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun performAction(bookToUpdate: LibraryBook, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val profile = authRepository.getProfile()
                if (profile == null) {
                    onComplete(false, "User profile not found. Please log in again.")
                    return@launch
                }
                val userId = profile.id

                val response = if (bookToUpdate.tab == LibraryTab.Available) {
                    repository.borrowBook(bookToUpdate.id, userId)
                } else {
                    repository.returnBook(bookToUpdate.id, userId)
                }

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        loadBooks() // Refresh
                        val action = if (bookToUpdate.tab == LibraryTab.Available) "borrowed" else "returned"
                        onComplete(true, "Successfully $action \"${bookToUpdate.title}\"")
                    } else {
                        onComplete(false, apiResponse?.message ?: "Operation failed")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = try {
                        gson.fromJson(errorBody, ApiResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    val errorMessage = errorResponse?.error?.message 
                        ?: errorResponse?.message 
                        ?: "Error ${response.code()}: ${response.message()}"
                    onComplete(false, errorMessage)
                }
            } catch (e: Exception) {
                onComplete(false, e.message ?: "An error occurred")
            }
        }
    }
}
