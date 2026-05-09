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

class CoursesViewModel(
    private val authRepository: com.example.studentapp.domain.repository.AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl(),
    private val enrollmentRepository: com.example.studentapp.domain.repository.EnrollmentRepository = com.example.studentapp.data.repository.EnrollmentRepositoryImpl()
) : ViewModel() {
    
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
            val profile = authRepository.getProfile()
            if (profile != null) {
                val studentId = profile.accountId
                
                // 1. Fetch Enrolled Courses from Study Load
                val studyLoad = enrollmentRepository.getStudyLoad(studentId)
                val enrolledEntries = studyLoad?.courses
                    ?.distinctBy { it.code }
                    ?.map { response ->
                    CourseEntry(
                        code = response.code,
                        title = response.title,
                        semesterTitle = response.semesterTitle ?: "",
                        instructor = response.instructor ?: "TBA",
                        units = response.units?.let { "$it UNITS" },
                        schedule = response.schedule,
                        location = response.location,
                        progress = response.progress ?: 0f,
                        status = CourseStatus.Enrolled
                    )
                } ?: emptyList()

                val enrolledCodes = enrolledEntries.map { it.code }.toSet()

                // 2. Fetch Completed Courses from Grade Records
                val gradeRecords = academicRepository.getGradeRecords(studentId)
                val completedEntries = gradeRecords
                    .filter { it.status == "COMPLETED" }
                    .map { response ->
                        val code = response.codeCredits.split(" • ").firstOrNull() ?: ""
                        CourseEntry(
                            code = code,
                            title = response.title,
                            semesterTitle = response.semesterLabel ?: "TBA",
                            instructor = "TBA", // Not in grade record
                            grade = "Grade: ${response.gradePoint}",
                            progress = 1.0f,
                            status = CourseStatus.Completed
                        )
                    }
                    .distinctBy { it.code }

                val completedCodes = completedEntries.map { it.code }.toSet()

                // 3. Fetch Waitlisted Courses from general list
                val allCourseList = academicRepository.getCourses()
                val waitlistedEntries = allCourseList
                    .filter { it.status == "Waitlisted" && !enrolledCodes.contains(it.code) && !completedCodes.contains(it.code) }
                    .distinctBy { it.code }
                    .map { response ->
                        CourseEntry(
                            code = response.code,
                            title = response.title,
                            semesterTitle = response.semesterTitle ?: "",
                            instructor = response.instructor ?: "TBA",
                            units = response.units?.let { "$it UNITS" },
                            schedule = response.schedule,
                            location = response.location,
                            waitlistStatus = response.waitlistStatus?.let { "Status: $it" } ?: "Status: Waitlisted",
                            progress = response.progress ?: 0f,
                            status = CourseStatus.Waitlisted
                        )
                    }

                allCourses = enrolledEntries + completedEntries + waitlistedEntries
            }
            isLoading = false
        }
    }
}
