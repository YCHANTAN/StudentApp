package com.example.studentapp.ui.screens.academic.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class AcademicDashboardMenuItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)

const val ACADEMIC_MENU_PROGRAMS = "programs"
const val ACADEMIC_MENU_COURSES = "courses"
const val ACADEMIC_MENU_ENROLLMENT = "enrollment"
const val ACADEMIC_MENU_GRADES = "grades"
const val ACADEMIC_MENU_EVALUATION = "eval"
const val ACADEMIC_MENU_STUDY_LOAD = "studyload"

fun buildAcademicDashboardMenuItems(): List<AcademicDashboardMenuItem> {
    return listOf(
        AcademicDashboardMenuItem(
            id = ACADEMIC_MENU_PROGRAMS,
            label = "Programs",
            icon = Icons.Outlined.School
        ),
        AcademicDashboardMenuItem(
            id = ACADEMIC_MENU_COURSES,
            label = "Courses",
            icon = Icons.Outlined.MenuBook
        ),
        AcademicDashboardMenuItem(
            id = ACADEMIC_MENU_ENROLLMENT,
            label = "Enrollment",
            icon = Icons.Outlined.Person
        ),
        AcademicDashboardMenuItem(
            id = ACADEMIC_MENU_GRADES,
            label = "Grades",
            icon = Icons.Outlined.List
        ),
        AcademicDashboardMenuItem(
            id = ACADEMIC_MENU_EVALUATION,
            label = "Evaluations",
            icon = Icons.Outlined.Check
        ),
        AcademicDashboardMenuItem(
            id = ACADEMIC_MENU_STUDY_LOAD,
            label = "Study Load",
            icon = Icons.Outlined.Schedule
        )
    )
}
