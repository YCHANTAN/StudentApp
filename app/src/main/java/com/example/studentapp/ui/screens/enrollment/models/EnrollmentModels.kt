package com.example.studentapp.ui.screens.enrollment.models

import androidx.compose.runtime.Immutable

@Immutable
data class EnrollableCourse(
    val id: String,
    val code: String,
    val title: String,
    val instructor: String,
    val schedule: String,
    val units: Int,
    val tuition: Double,
    val remainingSlots: Int,
    val isLocked: Boolean = false,
    val lockReason: String? = null
)

@Immutable
data class EnrollmentConfirmationCourse(
    val title: String,
    val subtitle: String,
    val iconType: EnrollmentConfirmationCourseIcon
)

enum class EnrollmentConfirmationCourseIcon {
    Code,
    Database,
    Design,
    Generic
}

enum class EnrollmentStep(
    val stepNumber: Int,
    val title: String,
    val progressLabel: String,
    val progressPercentageLabel: String,
    val progressFraction: Float
) {
    Courses(
        stepNumber = 1,
        title = "Course Enrollment",
        progressLabel = "Step 1 of 4: Courses",
        progressPercentageLabel = "25% Complete",
        progressFraction = 0.25f
    ),
    PersonalInfo(
        stepNumber = 2,
        title = "Student Enrollment",
        progressLabel = "Step 2 of 4: Personal Info",
        progressPercentageLabel = "50% Complete",
        progressFraction = 0.50f
    ),
    Payment(
        stepNumber = 3,
        title = "Enrollment Review",
        progressLabel = "Step 3 of 4: Payment",
        progressPercentageLabel = "75% Complete",
        progressFraction = 0.75f
    ),
    Confirmation(
        stepNumber = 4,
        title = "Enrollment Confirmation",
        progressLabel = "Step 4 of 4: Confirmation",
        progressPercentageLabel = "100% Complete",
        progressFraction = 1.0f
    )
}

fun filterEnrollableCourses(
    courses: List<EnrollableCourse>,
    searchQuery: String
): List<EnrollableCourse> {
    val normalizedQuery = searchQuery.trim()

    if (normalizedQuery.isBlank()) {
        return courses
    }

    return courses.filter { course ->
        course.code.contains(normalizedQuery, ignoreCase = true) ||
            course.title.contains(normalizedQuery, ignoreCase = true)
    }
}

fun buildEnrollmentConfirmationCourses(
    courses: List<EnrollableCourse>
): List<EnrollmentConfirmationCourse> {
    return courses.map { course ->
        EnrollmentConfirmationCourse(
            title = toConfirmationTitle(course.title),
            subtitle = "${course.code} • ${course.units} Credits",
            iconType = toConfirmationIconType(course.title)
        )
    }
}

private fun toConfirmationTitle(title: String): String {
    return when (title) {
        "Advanced Algorithms" -> "Adv Algorithms"
        "Database Management" -> "Database Mgmt"
        "User Interface Design" -> "UI Design"
        else -> title
    }
}

private fun toConfirmationIconType(title: String): EnrollmentConfirmationCourseIcon {
    return when {
        "algorithm" in title.lowercase() -> EnrollmentConfirmationCourseIcon.Code
        "database" in title.lowercase() -> EnrollmentConfirmationCourseIcon.Database
        "design" in title.lowercase() -> EnrollmentConfirmationCourseIcon.Design
        else -> EnrollmentConfirmationCourseIcon.Generic
    }
}
