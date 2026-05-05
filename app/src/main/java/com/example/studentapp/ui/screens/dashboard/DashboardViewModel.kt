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
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl()
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
                
                val formattedBalance = if (balance != null) {
                    val format = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
                    format.format(balance)
                } else {
                    "N/A"
                }

                val courseSnapshots = schedule.take(2).map { entry ->
                    CourseSnapshot(
                        code = entry.courseCode,
                        title = entry.courseTitle,
                        schedule = "${entry.day} | ${entry.timeRange}"
                    )
                }

                state = state.copy(
                    studentName = profile.fullName,
                    studentId = profile.accountId,
                    stats = state.stats.map { stat ->
                        if (stat.label == "Current Balance") {
                            stat.copy(value = formattedBalance)
                        } else {
                            stat
                        }
                    },
                    courses = if (courseSnapshots.isNotEmpty()) courseSnapshots else state.courses
                )
            }
            isLoading = false
        }
    }
}
