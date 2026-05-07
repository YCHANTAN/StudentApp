package com.example.studentapp.ui.screens.courses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.ui.screens.courses.models.CourseEntry
import com.example.studentapp.ui.screens.courses.models.CourseStatus
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl()
    
    var allCourses by mutableStateOf<List<CourseEntry>>(emptyList())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            isLoading = true
            val courses = academicRepository.getCourses()
            allCourses = courses.map { response ->
                CourseEntry(
                    code = response.code,
                    title = response.title,
                    semesterTitle = response.semesterTitle ?: "",
                    instructor = response.instructor ?: "Unknown",
                    units = response.units?.let { "$it UNITS" },
                    schedule = response.schedule,
                    location = response.location,
                    grade = response.grade?.let { "Grade: $it" },
                    waitlistStatus = response.waitlistStatus?.let { "Status: $it" },
                    progress = response.progress ?: 0f,
                    status = when (response.status) {
                        "Enrolled" -> CourseStatus.Enrolled
                        "Completed" -> CourseStatus.Completed
                        "Waitlisted" -> CourseStatus.Waitlisted
                        else -> CourseStatus.Enrolled
                    }
                )
            }
            isLoading = false
        }
    }
}
