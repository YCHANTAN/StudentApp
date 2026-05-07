package com.example.studentapp.navigation

sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object Dashboard : AppDestination("dashboard")
    data object StudyLoad : AppDestination("studyload")
    data object Grades : AppDestination("grades")
    data object Evaluation : AppDestination("eval")
    data object Academic : AppDestination("academic")
    data object Programs : AppDestination("programs")
    data object Courses : AppDestination("courses")
    data object Enrollment : AppDestination("enrollment")
    data object Adjustment : AppDestination("adjust")
    data object ChangeSchedule : AppDestination("sched")
    data object Schedule : AppDestination("schedule")
    data object Finance : AppDestination("finance")
    data object Services : AppDestination("services")
    data object Profile : AppDestination("profile")
    data object Library : AppDestination("library/{tab}") {
        fun createRoute(tab: String) = "library/$tab"
    }
    data object TOR : AppDestination("tor")
    data object COE : AppDestination("coe")
    data object GoodMoral : AppDestination("good-moral")
    data object Assessment : AppDestination("assessment")
    data object PaymentSlip : AppDestination("payment-slip")
    data object PaymentQueue : AppDestination("payment-queue")
    data object Notifications : AppDestination("notifications")
}
