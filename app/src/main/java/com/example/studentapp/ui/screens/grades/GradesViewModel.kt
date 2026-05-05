package com.example.studentapp.ui.screens.grades

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.ui.screens.grades.models.GradesSubjectItem
import com.example.studentapp.ui.screens.grades.models.SubjectIconType
import com.example.studentapp.ui.screens.grades.models.SubjectStatus
import kotlinx.coroutines.launch

class GradesViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl()
) : ViewModel() {
    var allSubjects by mutableStateOf<List<GradesSubjectItem>>(emptyList())
        private set
    
    var filteredSubjects by mutableStateOf<List<GradesSubjectItem>>(emptyList())
        private set

    var cumulativeGpa by mutableStateOf("0.00")
        private set

    var academicLevel by mutableStateOf("N/A")
        private set

    var semesters by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedSemester by mutableStateOf<String?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadGrades()
    }

    fun loadGrades() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                academicLevel = profile.programSummary.split("•").lastOrNull()?.trim() ?: "N/A"
                val gradeRecords = academicRepository.getGradeRecords(profile.accountId)
                
                allSubjects = gradeRecords.map { record ->
                    GradesSubjectItem(
                        title = record.title,
                        codeCredits = record.codeCredits,
                        gradePoint = record.gradePoint,
                        status = when (record.status.uppercase()) {
                            "COMPLETED" -> SubjectStatus.COMPLETED
                            "IN_PROGRESS" -> SubjectStatus.IN_PROGRESS
                            else -> SubjectStatus.COMPLETED
                        },
                        iconType = SubjectIconType.CODE,
                        semester = record.semesterLabel ?: "Unknown"
                    )
                }

                // Calculate GPA
                calculateGpa()

                // Extract and sort semesters
                semesters = allSubjects.map { it.semester }.distinct().sortedWith(SemesterComparator())
                
                if (selectedSemester == null || selectedSemester !in semesters) {
                    selectedSemester = semesters.firstOrNull()
                }
                
                updateFilteredSubjects()
            }
            isLoading = false
        }
    }

    fun selectSemester(semester: String) {
        selectedSemester = semester
        updateFilteredSubjects()
    }

    private fun updateFilteredSubjects() {
        filteredSubjects = if (selectedSemester != null) {
            allSubjects.filter { it.semester == selectedSemester }
        } else {
            allSubjects
        }
    }

    private fun calculateGpa() {
        val completedCourses = allSubjects.filter { it.status == SubjectStatus.COMPLETED }
        if (completedCourses.isEmpty()) {
            cumulativeGpa = "0.00"
            return
        }

        var totalPoints = 0.0
        var totalCredits = 0.0

        completedCourses.forEach { subject ->
            val grade = subject.gradePoint.toDoubleOrNull() ?: 0.0
            val credits = subject.codeCredits.split("•").lastOrNull()?.trim()
                ?.replace(" Credits", "")?.replace(" Credit", "")
                ?.toDoubleOrNull() ?: 0.0
            
            if (grade > 0 && credits > 0) {
                totalPoints += (grade * credits)
                totalCredits += credits
            }
        }

        cumulativeGpa = if (totalCredits > 0) {
            String.format("%.2f", totalPoints / totalCredits)
        } else {
            "0.00"
        }
    }

    private class SemesterComparator : Comparator<String> {
        override fun compare(s1: String, s2: String): Int {
            val r1 = parse(s1)
            val r2 = parse(s2)
            
            // Year ascending
            if (r1.year != r2.year) return r1.year - r2.year
            // Semester ascending
            return r1.sem - r2.sem
        }

        private fun parse(s: String): SemesterInfo {
            // Semester parsing: 1st, 2nd, 3rd, Summer
            val sem = when {
                s.contains("1st", ignoreCase = true) -> 1
                s.contains("2nd", ignoreCase = true) -> 2
                s.contains("3rd", ignoreCase = true) -> 3
                s.contains("Summer", ignoreCase = true) -> 4
                else -> 0
            }
            
            // Year parsing: "1st Year", "2nd Year", etc.
            val yearMatch = Regex("(\\d+)(?:st|nd|rd|th)\\s+Year", RegexOption.IGNORE_CASE).find(s)
            val year = yearMatch?.groupValues?.get(1)?.toInt() ?: run {
                // Fallback: search for any number if the above fails
                Regex("(\\d+)").find(s)?.groupValues?.get(1)?.toInt() ?: 0
            }
            
            return SemesterInfo(year, sem)
        }
        
        data class SemesterInfo(val year: Int, val sem: Int)
    }
}

