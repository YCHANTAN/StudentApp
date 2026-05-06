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
import java.text.NumberFormat
import java.util.Locale

class DashboardViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val financeRepository: FinanceRepository = com.example.studentapp.data.repository.FinanceRepositoryImpl(),
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl(),
    private val documentRepository: com.example.studentapp.domain.repository.DocumentRequestRepository = com.example.studentapp.data.repository.DocumentRequestRepositoryImpl()
) : ViewModel() {
    var state by mutableStateOf(buildDashboardUiState())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                val balance = financeRepository.getBalance(profile.accountId)
                val schedule = academicRepository.getScheduleEntries(profile.accountId)
                val documentRequests = documentRepository.getDocumentRequests(profile.accountId)
                val gradeRecords = academicRepository.getGradeRecords(profile.accountId)
                
                val formattedBalance = if (balance != null) {
                    val format = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
                    format.format(balance)
                } else {
                    "N/A"
                }

                // Calculate GPA and Units with more resilient logic
                val completedGrades = gradeRecords.filter { 
                    it.status.trim().equals("COMPLETED", ignoreCase = true) 
                }
                
                val totalUnits = completedGrades.sumOf { grade ->
                    // Try to find a number in the codeCredits string (e.g., "CS201 • 3 Credits" -> 3)
                    val unitsRegex = Regex("(\\d+)\\s*Credits?")
                    val match = unitsRegex.find(grade.codeCredits)
                    match?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
                }

                val gpa = if (completedGrades.isNotEmpty()) {
                    val sumPoints = completedGrades.sumOf { it.gradePoint.toDoubleOrNull() ?: 0.0 }
                    sumPoints / completedGrades.size
                } else {
                    // Fallback to initial values if we have profile but no records yet
                    // or if the calculation resulted in 0 but we know there should be some
                    if (state.stats.any { it.label == "GPA" && it.value != "0.00" }) {
                         state.stats.find { it.label == "GPA" }?.value?.toDoubleOrNull() ?: 0.0
                    } else 0.0
                }

                val finalUnits = if (totalUnits == 0.0 && state.stats.any { it.label == "Units Completed" && it.value != "0" }) {
                    state.stats.find { it.label == "Units Completed" }?.value?.toIntOrNull() ?: 0
                } else totalUnits.toInt()

                val courseSnapshots = schedule.take(2).map { entry ->
                    CourseSnapshot(
                        code = entry.courseCode,
                        title = entry.courseTitle,
                        schedule = "${entry.day} | ${entry.timeRange}"
                    )
                }

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
                            "Units Completed" -> stat.copy(value = finalUnits.toString())
                            else -> stat
                        }
                    },
                    courses = if (courseSnapshots.isNotEmpty()) courseSnapshots else state.courses,
                    requestStatus = requestStatus
                )
            }
            isLoading = false
        }
    }
}
