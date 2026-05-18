package com.example.studentapp.ui.screens.programs.models

import androidx.compose.runtime.Immutable

@Immutable
data class ProgramEntry(
    val id: String,
    val title: String,
    val badgeText: String,
    val badgeVariant: ProgramBadgeVariant,
    val scheduleLine: String,
    val description: String,
    val category: ProgramCategory,
    val prospectusUrl: String? = null
)

enum class ProgramBadgeVariant {
    Success,
    Info,
    Neutral
}

enum class ProgramCategory {
    Undergraduate,
    Postgraduate
}

enum class ProgramsTab(val label: String) {
    AllPrograms("All Programs"),
    Undergraduate("Undergraduate"),
    Postgraduate("Postgraduate")
}

fun buildProgramEntries(): List<ProgramEntry> {
    val sampleUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
    return listOf(
        ProgramEntry(
            id = "1",
            title = "BS Computer Science",
            badgeText = "Enrollment Open",
            badgeVariant = ProgramBadgeVariant.Success,
            scheduleLine = "4 Years \u2022 Full Time",
            description = "Master software engineering, AI, and cybersecurity with our industry-leading curriculum and hands-on laboratory modules.",
            category = ProgramCategory.Undergraduate,
            prospectusUrl = sampleUrl
        ),
        ProgramEntry(
            id = "2",
            title = "BBA Business Admin",
            badgeText = "Next Intake: Sept 2024",
            badgeVariant = ProgramBadgeVariant.Info,
            scheduleLine = "4 Years \u2022 Global Track",
            description = "Equip yourself with strategic leadership skills, financial acumen, and entrepreneurial mindset for the modern corporate world.",
            category = ProgramCategory.Undergraduate,
            prospectusUrl = sampleUrl
        ),
        ProgramEntry(
            id = "3",
            title = "MS Data Analytics",
            badgeText = "Enrollment Open",
            badgeVariant = ProgramBadgeVariant.Success,
            scheduleLine = "2 Years \u2022 Postgraduate",
            description = "Advanced statistical modeling and machine learning applications for driving data-informed business decisions.",
            category = ProgramCategory.Postgraduate,
            prospectusUrl = sampleUrl
        ),
        ProgramEntry(
            id = "4",
            title = "BS Architecture",
            badgeText = "Limited Seats",
            badgeVariant = ProgramBadgeVariant.Neutral,
            scheduleLine = "5 Years \u2022 Studio Based",
            description = "Explore sustainable design, urban planning, and historic conservation through creative architectural practice.",
            category = ProgramCategory.Undergraduate,
            prospectusUrl = sampleUrl
        )
    )
}

fun filterProgramEntries(
    programs: List<ProgramEntry>,
    searchQuery: String,
    selectedTab: ProgramsTab
): List<ProgramEntry> {
    val normalizedQuery = searchQuery.trim()

    return programs.filter { entry ->
        val matchesTab = when (selectedTab) {
            ProgramsTab.AllPrograms -> true
            ProgramsTab.Undergraduate -> entry.category == ProgramCategory.Undergraduate
            ProgramsTab.Postgraduate -> entry.category == ProgramCategory.Postgraduate
        }

        val matchesQuery = normalizedQuery.isBlank() ||
            entry.title.contains(normalizedQuery, ignoreCase = true) ||
            entry.description.contains(normalizedQuery, ignoreCase = true)

        matchesTab && matchesQuery
    }
}
