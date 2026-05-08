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
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl(),
    private val enrollmentRepository: com.example.studentapp.domain.repository.EnrollmentRepository = com.example.studentapp.data.repository.EnrollmentRepositoryImpl()
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
                val studentId = profile.accountId
                
                // Use the unified study load endpoint for consistency
                val studyLoad = enrollmentRepository.getStudyLoad(studentId)

                if (studyLoad != null && studyLoad.courses.isNotEmpty()) {
                    val allDayEntries = mutableListOf<Pair<String, ScheduleEntry>>()
                    
                    studyLoad.courses.distinctBy { it.code }.forEach { course ->
                        val scheduleStr = course.schedule ?: return@forEach
                        
                        // Parse "Mon/Wed 10:00 AM — 11:30 AM" or "Friday 09:00 AM — 12:00 PM"
                        // Handle potential formats
                        val timeRegex = Regex("""\d{1,2}:\d{2}\s*(?:AM|PM).*""")
                        val match = timeRegex.find(scheduleStr)
                        
                        if (match != null) {
                            val timePart = match.value.trim()
                            val daysPart = scheduleStr.substring(0, match.range.first).trim()
                            
                            val days = daysPart.split("/", " ", ",")
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }

                            days.forEach { dayStr ->
                                val fullDay = when(dayStr.uppercase()) {
                                    "MON", "MONDAY" -> "Monday"
                                    "TUE", "TUESDAY" -> "Tuesday"
                                    "WED", "WEDNESDAY" -> "Wednesday"
                                    "THU", "THURSDAY" -> "Thursday"
                                    "FRI", "FRIDAY" -> "Friday"
                                    "SAT", "SATURDAY" -> "Saturday"
                                    "SUN", "SUNDAY" -> "Sunday"
                                    else -> dayStr
                                }
                                
                                allDayEntries.add(fullDay to ScheduleEntry(
                                    courseCode = course.code,
                                    courseTitle = course.title,
                                    timeRange = timePart,
                                    room = course.location ?: "TBA",
                                    instructor = course.instructor ?: "TBA"
                                ))
                            }
                        }
                    }

                    // Group by day
                    val sections = allDayEntries
                        .groupBy({ it.first }, { it.second })
                        .map { (day, entries) ->
                            ScheduleDaySection(
                                dayLabel = day,
                                entries = entries.distinctBy { "${it.courseCode}-${it.timeRange}" }
                            )
                        }
                        .sortedBy { dayToOrder(it.dayLabel) }

                    state = state.copy(
                        studentName = profile.fullName,
                        sections = sections
                    )
                } else {
                    state = state.copy(
                        studentName = profile.fullName,
                        sections = emptyList()
                    )
                }
            }
            isLoading = false
        }
    }

    private fun dayToOrder(day: String): Int = when(day) {
        "Monday" -> 1
        "Tuesday" -> 2
        "Wednesday" -> 3
        "Thursday" -> 4
        "Friday" -> 5
        "Saturday" -> 6
        "Sunday" -> 7
        else -> 99
    }
}
