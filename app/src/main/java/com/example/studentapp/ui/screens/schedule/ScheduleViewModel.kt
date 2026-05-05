package com.example.studentapp.ui.screens.schedule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.ui.screens.schedule.models.ScheduleDaySection
import com.example.studentapp.ui.screens.schedule.models.ScheduleEntry
import com.example.studentapp.ui.screens.schedule.models.ScheduleUiState
import com.example.studentapp.ui.screens.schedule.models.buildScheduleUiState
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl()
) : ViewModel() {
    var state by mutableStateOf(buildScheduleUiState())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadSchedule()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                val scheduleEntries = academicRepository.getScheduleEntries(profile.accountId)
                
                val sections = scheduleEntries.groupBy { it.day }.map { (day, entries) ->
                    ScheduleDaySection(
                        dayLabel = day,
                        entries = entries.map { entry ->
                            ScheduleEntry(
                                courseCode = entry.courseCode,
                                courseTitle = entry.courseTitle,
                                timeRange = entry.timeRange,
                                room = entry.room,
                                instructor = entry.instructor
                            )
                        }
                    )
                }

                state = state.copy(
                    studentName = profile.fullName,
                    sections = if (sections.isNotEmpty()) sections else state.sections
                )
            }
            isLoading = false
        }
    }
}
