package com.example.studentapp.ui.screens.evaluations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.domain.repository.EnrollmentRepository
import com.example.studentapp.ui.screens.evaluations.models.EvaluationCourseIconType
import com.example.studentapp.ui.screens.evaluations.models.EvaluationCourseItem
import kotlinx.coroutines.launch

class EvaluationViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl(),
    private val enrollmentRepository: EnrollmentRepository = com.example.studentapp.data.repository.EnrollmentRepositoryImpl()
) : ViewModel() {
    var pendingCourses by mutableStateOf<List<EvaluationCourseItem>>(emptyList())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadPendingEvaluations()
    }

    fun loadPendingEvaluations() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                val studentId = profile.accountId
                try {
                    val studyLoad = enrollmentRepository.getStudyLoad(studentId)
                    val evaluations = academicRepository.getEvaluations(studentId)
                    val evaluatedCourseIds = evaluations.map { it.courseId }.toSet()

                    if (studyLoad != null && studyLoad.courses.isNotEmpty()) {
                        pendingCourses = studyLoad.courses
                            .filter { !evaluatedCourseIds.contains(it.id) }
                            .map { response ->
                                EvaluationCourseItem(
                                    id = response.id,
                                    codeTitle = "${response.code} - ${response.title}",
                                    instructor = response.instructor ?: "TBA",
                                    iconType = if (response.code.contains("CS")) EvaluationCourseIconType.DOCUMENT else EvaluationCourseIconType.CHART
                                )
                            }
                    } else {
                        pendingCourses = emptyList()
                    }
                } catch (e: Exception) {
                    pendingCourses = emptyList()
                }
            }
            isLoading = false
        }
    }

    fun submitEvaluation(courseId: String) {
        viewModelScope.launch {
            val profile = authRepository.getProfile()
            val item = pendingCourses.find { it.id == courseId }
            if (profile != null && item != null) {
                val success = academicRepository.submitEvaluation(
                    profile.accountId,
                    courseId,
                    item.teachingQuality,
                    item.courseMaterials,
                    item.punctuality,
                    item.comments
                )
                if (success) {
                    pendingCourses = pendingCourses.filter { it.id != courseId }
                }
            }
        }
    }

    fun toggleExpansion(courseId: String) {
        pendingCourses = pendingCourses.map {
            if (it.id == courseId) it.copy(isExpanded = !it.isExpanded)
            else it.copy(isExpanded = false) // Collapse others
        }
    }

    fun updateTeachingQuality(courseId: String, rating: Int) {
        pendingCourses = pendingCourses.map {
            if (it.id == courseId) it.copy(teachingQuality = rating) else it
        }
    }

    fun updateCourseMaterials(courseId: String, rating: Int) {
        pendingCourses = pendingCourses.map {
            if (it.id == courseId) it.copy(courseMaterials = rating) else it
        }
    }

    fun updatePunctuality(courseId: String, rating: Int) {
        pendingCourses = pendingCourses.map {
            if (it.id == courseId) it.copy(punctuality = rating) else it
        }
    }

    fun updateComments(courseId: String, comments: String) {
        pendingCourses = pendingCourses.map {
            if (it.id == courseId) it.copy(comments = comments) else it
        }
    }
}
