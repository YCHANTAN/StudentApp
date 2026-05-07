package com.example.studentapp.ui.screens.adjustment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.repository.AcademicRepositoryImpl
import com.example.studentapp.data.repository.EnrollmentRepositoryImpl
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.domain.repository.EnrollmentRepository
import com.example.studentapp.ui.screens.adjustment.models.AdjustmentCourseIconType
import com.example.studentapp.ui.screens.adjustment.models.AdjustmentCourseItem
import kotlinx.coroutines.launch

data class AdjustmentUiState(
    val enrolledCourses: List<AdjustmentCourseItem> = emptyList(),
    val searchResults: List<AdjustmentCourseItem> = emptyList(),
    val enrollmentId: String? = null,
    val totalUnits: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showRemoveDialog: AdjustmentCourseItem? = null,
    val showScheduleDialog: AdjustmentCourseItem? = null,
    val alternativeCourses: List<AdjustmentCourseItem> = emptyList()
)

class AdjustmentViewModel : ViewModel() {
    private val academicRepository: AcademicRepository = AcademicRepositoryImpl()
    private val enrollmentRepository: EnrollmentRepository = EnrollmentRepositoryImpl()

    var uiState by mutableStateOf(AdjustmentUiState())
        private set

    fun loadEnrollment(studentId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val enrollments = enrollmentRepository.getEnrollments(studentId)
                // Filter for latest PENDING or APPROVED enrollment
                val activeEnrollment = enrollments
                    .filter { it.status != "REJECTED" }
                    .maxByOrNull { it.createdAt }
                
                if (activeEnrollment != null) {
                    val allCourses = academicRepository.getCourses()
                    val enrolled = allCourses.filter { activeEnrollment.courseIds.contains(it.id) }
                    
                    uiState = uiState.copy(
                        enrollmentId = activeEnrollment.id,
                        enrolledCourses = enrolled.map { it.toAdjustmentItem() },
                        totalUnits = enrolled.sumOf { it.units ?: 0 },
                        isLoading = false
                    )
                } else {
                    uiState = uiState.copy(isLoading = false, error = "No active enrollment found for this student.")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun searchCourses(query: String) {
        if (query.isBlank()) {
            uiState = uiState.copy(searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            try {
                val allCourses = academicRepository.getCourses()
                val enrolledIds = uiState.enrolledCourses.map { it.id }.toSet()
                
                val results = allCourses.filter { course ->
                    !enrolledIds.contains(course.id) && (
                        course.code.contains(query, ignoreCase = true) ||
                        course.title.contains(query, ignoreCase = true) ||
                        (course.instructor?.contains(query, ignoreCase = true) ?: false)
                    )
                }
                
                uiState = uiState.copy(searchResults = results.map { it.toAdjustmentItem() })
            } catch (e: Exception) {
                // Silently fail search errors or log them
            }
        }
    }

    fun addCourse(course: AdjustmentCourseItem) {
        val enrollmentId = uiState.enrollmentId
        if (enrollmentId == null) {
            uiState = uiState.copy(error = "No active enrollment to update.")
            return
        }
        
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null, searchResults = emptyList())
            try {
                val newCourseIds = uiState.enrolledCourses.map { it.id } + course.id
                val updated = enrollmentRepository.updateEnrollment(enrollmentId, newCourseIds)
                
                if (updated != null) {
                    val allCourses = academicRepository.getCourses()
                    val enrolled = allCourses.filter { updated.courseIds.contains(it.id) }
                    
                    uiState = uiState.copy(
                        enrolledCourses = enrolled.map { it.toAdjustmentItem() },
                        totalUnits = enrolled.sumOf { it.units ?: 0 },
                        isLoading = false
                    )
                } else {
                    uiState = uiState.copy(isLoading = false, error = "Failed to add course. Please try again.")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message ?: "An unexpected error occurred.")
            }
        }
    }

    private fun com.example.studentapp.data.remote.CourseResponse.toAdjustmentItem(): AdjustmentCourseItem {
        return AdjustmentCourseItem(
            id = this.id,
            title = "${this.code}: ${this.title}",
            scheduleAndUnits = "${this.schedule} • ${this.units} Units",
            professor = this.instructor ?: "TBA",
            iconType = if (this.code.startsWith("CS")) AdjustmentCourseIconType.MONITOR else AdjustmentCourseIconType.GRID
        )
    }

    fun requestChangeSchedule(item: AdjustmentCourseItem) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null, showScheduleDialog = item)
            try {
                // Find courses with same code (extract code from title)
                val code = item.title.substringBefore(":")
                val allCourses = academicRepository.getCourses()
                val alternatives = allCourses.filter { it.code == code && it.id != item.id }
                
                uiState = uiState.copy(
                    isLoading = false,
                    alternativeCourses = alternatives.map { it.toAdjustmentItem() }
                )
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message ?: "Failed to load alternatives.")
            }
        }
    }

    fun selectNewSchedule(newCourse: AdjustmentCourseItem) {
        val oldCourse = uiState.showScheduleDialog ?: return
        val enrollmentId = uiState.enrollmentId
        if (enrollmentId == null) {
            uiState = uiState.copy(showScheduleDialog = null, error = "No enrollment found.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null, showScheduleDialog = null)
            try {
                val newCourseIds = uiState.enrolledCourses.map { 
                    if (it.id == oldCourse.id) newCourse.id else it.id 
                }
                val updated = enrollmentRepository.updateEnrollment(enrollmentId, newCourseIds)
                
                if (updated != null) {
                    val allCourses = academicRepository.getCourses()
                    val enrolled = allCourses.filter { updated.courseIds.contains(it.id) }
                    
                    uiState = uiState.copy(
                        enrolledCourses = enrolled.map { it.toAdjustmentItem() },
                        totalUnits = enrolled.sumOf { it.units ?: 0 },
                        isLoading = false
                    )
                } else {
                    uiState = uiState.copy(isLoading = false, error = "Failed to update schedule.")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message ?: "Update failed.")
            }
        }
    }

    fun cancelScheduleChange() {
        uiState = uiState.copy(showScheduleDialog = null, alternativeCourses = emptyList())
    }

    fun requestRemove(item: AdjustmentCourseItem) {
        uiState = uiState.copy(showRemoveDialog = item, error = null)
    }

    fun confirmRemove() {
        val itemToRemove = uiState.showRemoveDialog ?: return
        val enrollmentId = uiState.enrollmentId
        
        if (enrollmentId == null) {
            uiState = uiState.copy(showRemoveDialog = null, error = "Cannot remove: Enrollment ID is missing.")
            return
        }
        
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, showRemoveDialog = null, error = null)
            try {
                val newCourseIds = uiState.enrolledCourses
                    .filter { it.id != itemToRemove.id }
                    .map { it.id }
                
                val updated = enrollmentRepository.updateEnrollment(enrollmentId, newCourseIds)
                
                if (updated != null) {
                    // Force refresh courses to ensure we have latest data
                    val allCourses = academicRepository.getCourses()
                    val enrolled = allCourses.filter { updated.courseIds.contains(it.id) }
                    
                    uiState = uiState.copy(
                        enrolledCourses = enrolled.map { it.toAdjustmentItem() },
                        totalUnits = enrolled.sumOf { it.units ?: 0 },
                        isLoading = false,
                        error = null
                    )
                } else {
                    uiState = uiState.copy(isLoading = false, error = "Server failed to update enrollment.")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message ?: "Failed to remove course.")
            }
        }
    }

    fun cancelRemove() {
        uiState = uiState.copy(showRemoveDialog = null)
    }
    
    fun saveChanges() {
        // In this simple implementation, remove happens immediately. 
        // If we want a batch save, we'd manage a local list of changes.
    }
}
