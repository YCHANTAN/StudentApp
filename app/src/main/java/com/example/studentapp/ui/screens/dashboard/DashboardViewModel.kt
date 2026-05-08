package com.example.studentapp.ui.screens.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.domain.repository.FinanceRepository
import com.example.studentapp.ui.screens.dashboard.models.CourseSnapshot
import com.example.studentapp.ui.screens.dashboard.models.DashboardUiState
import com.example.studentapp.ui.screens.dashboard.models.buildDashboardUiState
import kotlinx.coroutines.launch
import java.util.Locale

class DashboardViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val financeRepository: FinanceRepository = com.example.studentapp.data.repository.FinanceRepositoryImpl(),
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl(),
    private val enrollmentRepository: com.example.studentapp.domain.repository.EnrollmentRepository = com.example.studentapp.data.repository.EnrollmentRepositoryImpl(),
    private val documentRepository: com.example.studentapp.domain.repository.DocumentRequestRepository = com.example.studentapp.data.repository.DocumentRequestRepositoryImpl()
) : ViewModel() {
    var state by mutableStateOf(buildDashboardUiState())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadDashboardData()
    }

    fun refresh() {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                val studentId = profile.accountId
                val balance = financeRepository.getBalance(studentId)
                val documentRequests = documentRepository.getDocumentRequests(studentId)
                val gradeRecords = academicRepository.getGradeRecords(studentId)
                
                // Match StudyLoad logic: get study load from the unified endpoint
                val studyLoad = enrollmentRepository.getStudyLoad(studentId)

                val courseSnapshots = if (studyLoad != null && studyLoad.courses.isNotEmpty()) {
                    studyLoad.courses
                        .distinctBy { it.code }
                        .take(2)
                        .map { course ->
                            CourseSnapshot(
                                code = course.code,
                                title = course.title,
                                schedule = course.schedule ?: "TBA"
                            )
                        }
                } else {
                    emptyList()
                }

                val formattedBalance = if (balance != null) {
                    "₱${String.format(Locale.US, "%,.2f", balance)}"
                } else {
                    "₱0.00"
                }

                // Calculate GPA and Units from actual grade records
                val completedGrades = gradeRecords.filter { 
                    it.status.trim().equals("COMPLETED", ignoreCase = true) 
                }
                
                val totalUnits = completedGrades.sumOf { grade ->
                    val unitsRegex = Regex("(\\d+)\\s*Credits?")
                    val match = unitsRegex.find(grade.codeCredits)
                    match?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
                }

                val gpa = if (completedGrades.isNotEmpty()) {
                    val sumPoints = completedGrades.sumOf { it.gradePoint.toDoubleOrNull() ?: 0.0 }
                    sumPoints / completedGrades.size
                } else 0.0

                val latestRequest = documentRequests.firstOrNull()
                val requestStatus = if (latestRequest != null) {
                    com.example.studentapp.ui.screens.dashboard.models.ServiceRequestStatus(
                        title = "Request for ${latestRequest.type}",
                        reference = latestRequest.reference,
                        statusLabel = latestRequest.status,
                        progress = when(latestRequest.status.uppercase()) {
                            "SUBMITTED" -> 0.2f
                            "PROCESSING" -> 0.5f
                            "READY_FOR_PICKUP" -> 0.9f
                            "COMPLETED" -> 1.0f
                            else -> 0.1f
                        },
                        estimatedCompletion = "3-5 Business Days"
                    )
                } else {
                    state.requestStatus
                }

                state = state.copy(
                    studentName = profile.fullName,
                    studentId = profile.accountId,
                    idStatus = "Active",
                    stats = state.stats.map { stat ->
                        when (stat.label) {
                            "Current Balance" -> stat.copy(value = formattedBalance)
                            "GPA" -> stat.copy(value = String.format(Locale.US, "%.2f", gpa))
                            "Units Completed" -> stat.copy(value = totalUnits.toInt().toString())
                            else -> stat
                        }
                    },
                    courses = courseSnapshots,
                    requestStatus = requestStatus
                )
            }
            isLoading = false
        }
    }
}
