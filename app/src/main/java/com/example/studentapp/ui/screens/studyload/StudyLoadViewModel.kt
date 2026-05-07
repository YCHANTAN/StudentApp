package com.example.studentapp.ui.screens.studyload

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.ui.screens.studyload.models.StudyLoadItem
import kotlinx.coroutines.launch

class StudyLoadViewModel(
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl(),
    private val enrollmentRepository: com.example.studentapp.domain.repository.EnrollmentRepository = com.example.studentapp.data.repository.EnrollmentRepositoryImpl()
) : ViewModel() {
    var subjects by mutableStateOf<List<StudyLoadItem>>(emptyList())
        private set
    
    var totalUnits by mutableStateOf(0)
        private set
        
    var semesterLabel by mutableStateOf("N/A")
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadStudyLoad("user_123") // Hardcoded for prototype consistency
    }

    fun loadStudyLoad(studentId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val enrollments = enrollmentRepository.getEnrollments(studentId)
                val activeEnrollment = enrollments
                    .filter { it.status != "REJECTED" }
                    .maxByOrNull { it.createdAt }

                if (activeEnrollment != null) {
                    val allCourses = academicRepository.getCourses()
                    val enrolledCourses = allCourses.filter { activeEnrollment.courseIds.contains(it.id) }
                    
                    subjects = enrolledCourses.map { response ->
                        StudyLoadItem(
                            title = response.title,
                            code = response.code,
                            schedule = response.schedule ?: "TBA",
                            room = response.location ?: "TBA",
                            instructor = response.instructor ?: "TBA",
                            units = response.units ?: 0,
                            status = "Enrolled"
                        )
                    }
                    
                    totalUnits = subjects.sumOf { it.units }
                    semesterLabel = enrolledCourses.firstOrNull()?.semesterTitle ?: "Current Semester"
                } else {
                    subjects = emptyList()
                    totalUnits = 0
                    semesterLabel = "No Active Enrollment"
                }
            } catch (e: Exception) {
                // Handle error state
            } finally {
                isLoading = false
            }
        }
    }
}
