package com.example.studentapp.ui.screens.courses.models

import androidx.compose.runtime.Immutable

@Immutable
data class CourseEntry(
    val code: String,
    val title: String,
    val semesterTitle: String,
    val instructor: String,
    val units: String? = null,
    val schedule: String? = null,
    val location: String? = null,
    val grade: String? = null,
    val waitlistStatus: String? = null,
    val status: CourseStatus
)

enum class CourseStatus(val label: String) {
    Enrolled("Enrolled"),
    Completed("Completed"),
    Waitlisted("Waitlisted")
}

fun buildCourseEntries(): List<CourseEntry> {
    return listOf(
        CourseEntry(
            code = "CS301",
            title = "Advanced Algorithms",
            semesterTitle = "Spring Semester 2024",
            instructor = "Dr. Helena Vance",
            units = "4 UNITS",
            schedule = "Mon/Wed 10:00 AM \u2014 11:30 AM",
            location = "Engineering Hall, Rm 402",
            status = CourseStatus.Enrolled
        ),
        CourseEntry(
            code = "MATH402",
            title = "Stochastic Processes",
            semesterTitle = "Spring Semester 2024",
            instructor = "Prof. Julian Thorne",
            units = "3 UNITS",
            schedule = "Tue/Thu 01:00 PM \u2014 02:30 PM",
            location = "Science Building, Rm 105",
            status = CourseStatus.Enrolled
        ),
        CourseEntry(
            code = "CS320",
            title = "Machine Learning Basics",
            semesterTitle = "Spring Semester 2024",
            instructor = "Dr. Sarah Jenkins",
            units = "4 UNITS",
            schedule = "Friday 09:00 AM \u2014 12:00 PM",
            location = "Online Sync",
            status = CourseStatus.Enrolled
        ),
        CourseEntry(
            code = "CS201",
            title = "Data Structures",
            semesterTitle = "2nd Semester 3rd Year",
            instructor = "Dr. Alan Turing",
            grade = "Grade: 1.25",
            status = CourseStatus.Completed
        ),
        CourseEntry(
            code = "MATH302",
            title = "Linear Algebra",
            semesterTitle = "2nd Semester 3rd Year",
            instructor = "Prof. Katherine Johnson",
            grade = "Grade: 1.50",
            status = CourseStatus.Completed
        ),
        CourseEntry(
            code = "CS205",
            title = "Operating Systems",
            semesterTitle = "2nd Semester 3rd Year",
            instructor = "Dr. Grace Hopper",
            grade = "Grade: 1.00",
            status = CourseStatus.Completed
        ),
        CourseEntry(
            code = "CS401",
            title = "Artificial Intelligence",
            semesterTitle = "2nd Semester 3rd Year",
            instructor = "Prof. Robert Smith",
            units = "4 Units",
            schedule = "Mon/Wed 2:00 PM \u2014 3:30 PM",
            waitlistStatus = "Status: Waitlisted #15",
            status = CourseStatus.Waitlisted
        ),
        CourseEntry(
            code = "MATH501",
            title = "Advanced Calculus",
            semesterTitle = "2nd Semester 3rd Year",
            instructor = "Dr. Elena Gilbert",
            units = "3 Units",
            schedule = "Tue/Thu 9:00 AM \u2014 10:30 AM",
            waitlistStatus = "Status: Waitlisted #3",
            status = CourseStatus.Waitlisted
        )
    )
}

fun filterCourseEntries(
    courses: List<CourseEntry>,
    searchQuery: String,
    selectedStatus: CourseStatus
): List<CourseEntry> {
    val normalizedQuery = searchQuery.trim()

    return courses.filter { entry ->
        val matchesStatus = entry.status == selectedStatus
        val matchesQuery = normalizedQuery.isBlank() ||
            entry.title.contains(normalizedQuery, ignoreCase = true) ||
            entry.code.contains(normalizedQuery, ignoreCase = true)

        matchesStatus && matchesQuery
    }
}
