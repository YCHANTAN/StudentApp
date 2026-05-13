package com.example.studentapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.studentapp.ui.screens.finance.PaymentSlipScreen
import com.example.studentapp.ui.screens.goodmoral.GoodMoralScreen
import androidx.lifecycle.viewmodel.compose.viewModel
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
    var currentRoute by rememberSaveable {
        mutableStateOf(AppDestination.Login.route)
    }

    val primaryBottomNavItems = remember { buildPrimaryBottomNavItems() }

    val navigateToNotifications = {
        currentRoute = AppDestination.Notifications.route
    }

    when {
        currentRoute == AppDestination.Login.route -> {
            LoginScreen(
                onLoginSuccess = {
                    currentRoute = AppDestination.Dashboard.route
                }
            )
        }

        currentRoute == AppDestination.Dashboard.route -> {
            BackHandler {
                currentRoute = AppDestination.Login.route
            }
            DashboardScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "home",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onViewScheduleClick = {
                    currentRoute = AppDestination.Schedule.route
                },
                onFinanceClick = {
                    currentRoute = AppDestination.Finance.route
                },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute == AppDestination.Academic.route -> {
            BackHandler {
                currentRoute = AppDestination.Dashboard.route
            }

            AcademicScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = { currentRoute = AppDestination.Dashboard.route },
                onViewAllClick = { currentRoute = AppDestination.Dashboard.route },
                onContactSupportClick = { currentRoute = AppDestination.Dashboard.route },
                onCoursesClick = { currentRoute = AppDestination.Courses.route },
                onEnrollmentClick = { currentRoute = AppDestination.Enrollment.route },
                onProgramsClick = { currentRoute = AppDestination.Programs.route },
                onGradesClick = { currentRoute = AppDestination.Grades.route },
                onEvaluationClick = { currentRoute = AppDestination.Evaluation.route },
                onStudyLoadClick = { currentRoute = AppDestination.StudyLoad.route },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute == AppDestination.Programs.route -> {
            BackHandler {
                currentRoute = AppDestination.Academic.route
            }

            ProgramsScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Academic.route
                },
                onDownloadProspectusClick = {},
                onViewProgramClick = {}
            )
        }

        currentRoute == AppDestination.Courses.route -> {
            BackHandler {
                currentRoute = AppDestination.Academic.route
            }

            CoursesScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Academic.route
                }
            )
        }

        currentRoute == AppDestination.Enrollment.route -> {
            BackHandler {
                currentRoute = AppDestination.Academic.route
            }

            EnrollmentScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Academic.route
                },
                onHomeClick = {
                    currentRoute = AppDestination.Dashboard.route
                },
                onAdjustmentClick = {
                    currentRoute = AppDestination.Adjustment.route
                }
            )
        }

        currentRoute == AppDestination.Grades.route -> {
            BackHandler {
                currentRoute = AppDestination.Academic.route
            }
            GradesScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Academic.route
                }
            )
        }

        currentRoute == AppDestination.Evaluation.route -> {
            BackHandler {
                currentRoute = AppDestination.Academic.route
            }
            EvaluationScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Academic.route
                }
            )
        }

        currentRoute == AppDestination.StudyLoad.route -> {
            BackHandler {
                currentRoute = AppDestination.Academic.route
            }
            StudyLoadScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Academic.route
                }
            )
        }

        currentRoute == AppDestination.Adjustment.route -> {
            BackHandler {
                currentRoute = AppDestination.Dashboard.route
            }
            AdjustmentScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Dashboard.route
                }
            )
        }

        currentRoute == AppDestination.ChangeSchedule.route -> {
            BackHandler {
                currentRoute = AppDestination.Adjustment.route
            }
            ChangeScheduleScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "academic",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Adjustment.route
                },
                onConfirmClick = {
                    currentRoute = AppDestination.Adjustment.route
                }
            )
        }

        currentRoute == AppDestination.Finance.route -> {
            BackHandler {
                currentRoute = AppDestination.Dashboard.route
            }
            FinanceScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "finance",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Dashboard.route
                },
                onPayNowClick = {
                    currentRoute = AppDestination.PaymentQueue.route
                },
                onAssessmentClick = {
                    currentRoute = AppDestination.Assessment.route
                },
                onPaymentSlipClick = {
                    currentRoute = AppDestination.PaymentSlip.route
                },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute == AppDestination.Assessment.route -> {
            BackHandler {
                currentRoute = AppDestination.Finance.route
            }
            val financeViewModel: com.example.studentapp.ui.screens.finance.FinanceViewModel = viewModel()
            AssessmentScreen(
                assessment = financeViewModel.assessment,
                onBackClick = {
                    currentRoute = AppDestination.Finance.route
                }
            )
        }

        currentRoute == AppDestination.PaymentSlip.route -> {
            BackHandler {
                currentRoute = AppDestination.Finance.route
            }
            val financeViewModel: com.example.studentapp.ui.screens.finance.FinanceViewModel = viewModel()
            PaymentSlipScreen(
                paymentSlip = financeViewModel.paymentSlip,
                onBackClick = {
                    currentRoute = AppDestination.Finance.route
                }
            )
        }

        currentRoute == AppDestination.Services.route -> {
            BackHandler {
                currentRoute = AppDestination.Dashboard.route
            }
            ServicesScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Dashboard.route
                },
                onLibraryClick = { tab ->
                    currentRoute = AppDestination.Library.createRoute(tab.name)
                },
                onTORClick = {
                    currentRoute = AppDestination.TOR.route
                },
                onCOEClick = {
                    currentRoute = AppDestination.COE.route
                },
                onGoodMoralClick = {
                    currentRoute = AppDestination.GoodMoral.route
                },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute.startsWith("library/") -> {
            val tabName = currentRoute.substringAfter("library/")
            val initialTab = LibraryTab.entries.find { it.name == tabName } ?: LibraryTab.Available
            
            BackHandler {
                currentRoute = AppDestination.Services.route
            }
            LibraryScreen(
                initialTab = initialTab,
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Services.route
                },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute == AppDestination.COE.route -> {
            BackHandler {
                currentRoute = AppDestination.Services.route
            }

            COEScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Services.route
                },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute == AppDestination.GoodMoral.route -> {
            BackHandler {
                currentRoute = AppDestination.Services.route
            }

            GoodMoralScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Services.route
                },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute == AppDestination.TOR.route -> {
            BackHandler {
                currentRoute = AppDestination.Services.route
            }
            TORScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "services",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Services.route
                },
                onNotificationClick = navigateToNotifications
            )
        }

        currentRoute == AppDestination.PaymentQueue.route -> {
            BackHandler {
                currentRoute = AppDestination.Finance.route
            }

            PaymentQueueScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "finance",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Finance.route
                }
            )
        }

        currentRoute == AppDestination.Schedule.route -> {
            BackHandler {
                currentRoute = AppDestination.Dashboard.route
            }
            ScheduleScreen(
                onBackClick = {
                    currentRoute = AppDestination.Dashboard.route
                }
            )
        }

        currentRoute == AppDestination.Profile.route -> {
            BackHandler {
                currentRoute = AppDestination.Dashboard.route
            }
            ProfileScreen(
                navigationItems = primaryBottomNavItems,
                selectedNavItemId = "profile",
                onBottomNavSelected = { item ->
                    currentRoute = resolvePrimaryRoute(item, currentRoute)
                },
                onBackClick = {
                    currentRoute = AppDestination.Dashboard.route
                }
            )
        }

        currentRoute == AppDestination.Notifications.route -> {
            BackHandler {
                currentRoute = AppDestination.Dashboard.route 
            }
            NotificationScreen(
                onBackClick = {
                    currentRoute = AppDestination.Dashboard.route
                }
            )
        }
    }
}

private fun resolvePrimaryRoute(
    item: StudentBottomNavItem,
    currentRoute: String
): String {
    return when (item.id) {
        "home" -> AppDestination.Dashboard.route
        "academic" -> AppDestination.Academic.route
        "finance" -> AppDestination.Finance.route
        "services" -> AppDestination.Services.route
        "profile" -> AppDestination.Profile.route
        else -> currentRoute
    }
}
