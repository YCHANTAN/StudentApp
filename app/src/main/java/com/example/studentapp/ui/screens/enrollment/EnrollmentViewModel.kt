package com.example.studentapp.ui.screens.enrollment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.repository.AcademicRepositoryImpl
import com.example.studentapp.data.repository.EnrollmentRepositoryImpl
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.domain.repository.EnrollmentRepository
import com.example.studentapp.ui.screens.enrollment.models.EnrollableCourse
import kotlinx.coroutines.launch

data class EnrollmentUiState(
    val courses: List<EnrollableCourse> = emptyList(),
    val selectedCourseIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val enrollmentSuccess: Boolean = false
)

class EnrollmentViewModel : ViewModel() {
    private val academicRepository: AcademicRepository = AcademicRepositoryImpl()
    private val enrollmentRepository: EnrollmentRepository = EnrollmentRepositoryImpl()

    var uiState by mutableStateOf(EnrollmentUiState())
        private set

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val courses = academicRepository.getCourses()
                val mappedCourses = courses.map { 
                    EnrollableCourse(
                        id = it.id,
                        code = it.code,
                        title = it.title,
                        instructor = it.instructor ?: "TBA",
                        schedule = it.schedule ?: "TBA",
                        units = it.units ?: 0,
                        tuition = it.tuition ?: 0.0,
                        remainingSlots = it.remainingSlots ?: 0,
                        isLocked = false 
                    )
                }
                
                // Sort: available courses first
                val sortedCourses = mappedCourses.sortedByDescending { it.remainingSlots > 0 }
                
                uiState = uiState.copy(
                    courses = sortedCourses,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun toggleCourse(courseId: String) {
        val currentSelected = uiState.selectedCourseIds
        val course = uiState.courses.find { it.id == courseId }
        
        if (currentSelected.contains(courseId)) {
            uiState = uiState.copy(selectedCourseIds = currentSelected - courseId)
        } else if (course != null && course.remainingSlots > 0) {
            uiState = uiState.copy(selectedCourseIds = currentSelected + courseId)
        }
    }

    fun submitEnrollment(studentId: String) {
        if (uiState.selectedCourseIds.isEmpty()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val result = enrollmentRepository.createEnrollment(
                    studentId = studentId,
                    courseIds = uiState.selectedCourseIds.toList()
                )
                if (result != null) {
                    uiState = uiState.copy(isLoading = false, enrollmentSuccess = true)
                } else {
                    uiState = uiState.copy(isLoading = false, error = "Failed to submit enrollment")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message)
            }
        }
    }
}
