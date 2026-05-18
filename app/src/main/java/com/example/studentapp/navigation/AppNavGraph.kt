package com.example.studentapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.academic.AcademicScreen
import com.example.studentapp.ui.screens.adjustment.AdjustmentScreen
import com.example.studentapp.ui.screens.changeschedule.ChangeScheduleScreen
import com.example.studentapp.ui.screens.coe.COEScreen
import com.example.studentapp.ui.screens.courses.CoursesScreen
import com.example.studentapp.ui.screens.dashboard.DashboardScreen
import com.example.studentapp.ui.screens.enrollment.EnrollmentScreen
import com.example.studentapp.ui.screens.evaluations.EvaluationScreen
import com.example.studentapp.ui.screens.finance.FinanceScreen
import com.example.studentapp.ui.screens.finance.AssessmentScreen
import com.example.studentapp.ui.screens.goodmoral.GoodMoralScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentapp.ui.screens.grades.GradesScreen
import com.example.studentapp.ui.screens.library.LibraryScreen
import com.example.studentapp.ui.screens.library.models.LibraryTab
import com.example.studentapp.ui.screens.login.LoginScreen
import com.example.studentapp.ui.screens.notifications.NotificationScreen
import com.example.studentapp.ui.screens.payment.PaymentQueueScreen
import com.example.studentapp.ui.screens.profile.ProfileScreen
import com.example.studentapp.ui.screens.programs.ProgramsScreen
import com.example.studentapp.ui.screens.schedule.ScheduleScreen
import com.example.studentapp.ui.screens.services.ServicesScreen
import com.example.studentapp.ui.screens.studyload.StudyLoadScreen
import com.example.studentapp.ui.screens.tor.TORScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val primaryBottomNavItems = remember { buildPrimaryBottomNavItems() }

    val navigateToNotifications = {
        navController.navigate(AppDestination.Notifications.route)
    }

    val onBottomNavSelected: (StudentBottomNavItem) -> Unit = { item ->
        val route = resolvePrimaryRoute(item)
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route
    ) {
        composable(AppDestination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestination.Dashboard.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestination.Dashboard.route) {
            DashboardScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "home",
                onBottomNavSelected = onBottomNavSelected,
                onViewScheduleClick = {
                    navController.navigate(AppDestination.Schedule.route)
                },
                onFinanceClick = {
                    navController.navigate(AppDestination.Finance.route)
                },
                onGradesClick = {
                    navController.navigate(AppDestination.Grades.route)
                },
                onCoursesClick = {
                    navController.navigate(AppDestination.Courses.route)
                },
                onNotificationClick = navigateToNotifications
            )
        }

        composable(AppDestination.Academic.route) {
            AcademicScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onViewAllClick = { navController.popBackStack() },
                onContactSupportClick = { /* Handle or pop */ },
                onCoursesClick = { navController.navigate(AppDestination.Courses.route) },
                onEnrollmentClick = { navController.navigate(AppDestination.Enrollment.route) },
                onProgramsClick = { navController.navigate(AppDestination.Programs.route) },
                onGradesClick = { navController.navigate(AppDestination.Grades.route) },
                onEvaluationClick = { navController.navigate(AppDestination.Evaluation.route) },
                onStudyLoadClick = { navController.navigate(AppDestination.StudyLoad.route) },
                onNotificationClick = navigateToNotifications
            )
        }

        composable(AppDestination.Programs.route) {
            ProgramsScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onDownloadProspectusClick = {},
                onViewProgramClick = {}
            )
        }

        composable(AppDestination.Courses.route) {
            CoursesScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Enrollment.route) {
            EnrollmentScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onHomeClick = {
                    navController.navigate(AppDestination.Dashboard.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onAdjustmentClick = {
                    navController.navigate(AppDestination.Adjustment.route)
                }
            )
        }

        composable(AppDestination.Grades.route) {
            GradesScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Evaluation.route) {
            EvaluationScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.StudyLoad.route) {
            StudyLoadScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Adjustment.route) {
            AdjustmentScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.ChangeSchedule.route) {
            ChangeScheduleScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Finance.route) {
            FinanceScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "finance",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onPayNowClick = {
                    navController.navigate(AppDestination.PaymentQueue.route)
                },
                onAssessmentClick = {
                    navController.navigate(AppDestination.Assessment.route)
                },
                onNotificationClick = navigateToNotifications
            )
        }

        composable(AppDestination.Assessment.route) {
            val financeViewModel: com.example.studentapp.ui.screens.finance.FinanceViewModel = viewModel()
            AssessmentScreen(
                assessment = financeViewModel.assessment,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Services.route) {
            ServicesScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onLibraryClick = { tab ->
                    navController.navigate(AppDestination.Library.createRoute(tab.name))
                },
                onTORClick = {
                    navController.navigate(AppDestination.TOR.route)
                },
                onCOEClick = {
                    navController.navigate(AppDestination.COE.route)
                },
                onGoodMoralClick = {
                    navController.navigate(AppDestination.GoodMoral.route)
                },
                onNotificationClick = navigateToNotifications
            )
        }

        composable(
            route = AppDestination.Library.route,
            arguments = listOf(navArgument("tab") { type = NavType.StringType })
        ) { backStackEntry ->
            val tabName = backStackEntry.arguments?.getString("tab")
            val initialTab = LibraryTab.entries.find { it.name == tabName } ?: LibraryTab.Available
            
            LibraryScreen(
                initialTab = initialTab,
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onNotificationClick = navigateToNotifications
            )
        }

        composable(AppDestination.COE.route) {
            COEScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onNotificationClick = navigateToNotifications,
                onNavigateToFinance = {
                    navController.navigate(AppDestination.Finance.route) {
                        popUpTo(AppDestination.Dashboard.route)
                    }
                }
            )
        }

        composable(AppDestination.GoodMoral.route) {
            GoodMoralScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onNotificationClick = navigateToNotifications,
                onNavigateToFinance = {
                    navController.navigate(AppDestination.Finance.route) {
                        popUpTo(AppDestination.Dashboard.route)
                    }
                }
            )
        }

        composable(AppDestination.TOR.route) {
            TORScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() },
                onNotificationClick = navigateToNotifications,
                onNavigateToFinance = {
                    navController.navigate(AppDestination.Finance.route) {
                        popUpTo(AppDestination.Dashboard.route)
                    }
                }
            )
        }

        composable(AppDestination.PaymentQueue.route) {
            PaymentQueueScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "finance",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Schedule.route) {
            ScheduleScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Profile.route) {
            ProfileScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "profile",
                onBottomNavSelected = onBottomNavSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Notifications.route) {
            NotificationScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

private fun resolvePrimaryRoute(
    item: StudentBottomNavItem
): String {
    return when (item.id) {
        "home" -> AppDestination.Dashboard.route
        "academic" -> AppDestination.Academic.route
        "finance" -> AppDestination.Finance.route
        "services" -> AppDestination.Services.route
        "profile" -> AppDestination.Profile.route
        else -> AppDestination.Dashboard.route
    }
}
