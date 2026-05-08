package com.example.studentapp.ui.screens.enrollment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.screens.enrollment.components.EnrollmentBottomSheet
import com.example.studentapp.ui.screens.enrollment.components.EnrollmentConfirmationStepContent
import com.example.studentapp.ui.screens.enrollment.components.EnrollmentCourseStepContent
import com.example.studentapp.ui.screens.enrollment.components.EnrollmentHeaderSection
import com.example.studentapp.ui.screens.enrollment.components.EnrollmentPaymentStepContent
import com.example.studentapp.ui.screens.enrollment.components.EnrollmentPersonalInfoStepContent
import com.example.studentapp.ui.screens.enrollment.models.EnrollmentStep
import com.example.studentapp.ui.screens.enrollment.models.buildEnrollmentConfirmationCourses
import com.example.studentapp.ui.screens.enrollment.models.filterEnrollableCourses
import com.example.studentapp.ui.theme.StudentAppTheme

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

@Composable
fun EnrollmentScreen(
    navigationItems: List<StudentBottomNavItem>,
    selectedNavItemId: String,
    onBottomNavSelected: (StudentBottomNavItem) -> Unit,
    onBackClick: () -> Unit,
    onDownloadReceiptClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onAdjustmentClick: () -> Unit = {},
    viewModel: EnrollmentViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    var currentStep by rememberSaveable { mutableStateOf(EnrollmentStep.Courses) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    
    // ... rest of logic ...
    
    val filteredCourses = remember(searchQuery, uiState.courses) {
        filterEnrollableCourses(
            courses = uiState.courses,
            searchQuery = searchQuery
        )
    }
    val selectedCourses = remember(uiState.selectedCourseIds, uiState.courses) {
        uiState.courses.filter { course -> uiState.selectedCourseIds.contains(course.id) }
    }
    val selectedCredits = selectedCourses.sumOf { it.units }
    val estimatedTuition = selectedCourses.sumOf { it.tuition }
    val confirmationCourses = remember(selectedCourses) {
        buildEnrollmentConfirmationCourses(selectedCourses)
    }

    var fullName by rememberSaveable { mutableStateOf("") }
    var studentId by rememberSaveable { mutableStateOf("") }
    var emailAddress by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var emergencyContactName by rememberSaveable { mutableStateOf("") }
    var relationship by rememberSaveable { mutableStateOf("Parent") }
    var emergencyPhone by rememberSaveable { mutableStateOf("") }

    // Auto-fill from profile once loaded
    var hasAutoFilled by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(uiState.studentProfile) {
        if (uiState.studentProfile != null && !hasAutoFilled) {
            fullName = uiState.studentProfile.fullName
            studentId = uiState.studentProfile.accountId
            emailAddress = uiState.studentProfile.emailAddress
            phoneNumber = uiState.studentProfile.phoneNumber
            emergencyContactName = uiState.studentProfile.emergencyContact.name
            relationship = uiState.studentProfile.emergencyContact.relationship
            emergencyPhone = uiState.studentProfile.emergencyContact.phoneNumber
            hasAutoFilled = true
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AnimatedVisibility(
                visible = currentStep != EnrollmentStep.Confirmation,
                enter = fadeIn(animationSpec = tween(220)) +
                    slideInVertically(animationSpec = tween(280)) { -it / 4 },
                exit = fadeOut(animationSpec = tween(180)) +
                    slideOutVertically(animationSpec = tween(220)) { -it / 4 }
            ) {
                EnrollmentHeaderSection(
                    step = currentStep,
                    onBackClick = {
                        currentStep = previousEnrollmentStep(currentStep) ?: run {
                            onBackClick()
                            return@EnrollmentHeaderSection
                        }
                    }
                )
            }
        },
        bottomBar = {
            Column {
                AnimatedVisibility(
                    visible = currentStep == EnrollmentStep.Courses,
                    enter = fadeIn(animationSpec = tween(220)) +
                        slideInVertically(animationSpec = tween(280)) { it / 3 },
                    exit = fadeOut(animationSpec = tween(180)) +
                        slideOutVertically(animationSpec = tween(220)) { it / 3 }
                ) {
                    EnrollmentBottomSheet(
                        selectedCredits = selectedCredits,
                        maxCredits = 18,
                        estimatedTuition = estimatedTuition,
                        onNextClick = {
                            if (uiState.selectedCourseIds.isNotEmpty()) {
                                currentStep = EnrollmentStep.PersonalInfo
                            }
                        }
                    )
                }

                StudentBottomNavBar(
                    items = navigationItems,
                    selectedItemId = selectedNavItemId,
                    onItemSelected = onBottomNavSelected
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    val movingForward = targetState.stepNumber > initialState.stepNumber
                    (
                        slideInHorizontally(
                            animationSpec = tween(320),
                            initialOffsetX = { fullWidth ->
                                if (movingForward) fullWidth / 3 else -fullWidth / 3
                            }
                        ) + fadeIn(animationSpec = tween(320))
                    ).togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(280),
                            targetOffsetX = { fullWidth ->
                                if (movingForward) -fullWidth / 5 else fullWidth / 5
                            }
                        ) + fadeOut(animationSpec = tween(220))
                    ).using(SizeTransform(clip = false))
                },
                label = "enrollment_step_transition"
            ) { step ->
                when (step) {
                    EnrollmentStep.Courses -> {
                        EnrollmentCourseStepContent(
                            courses = filteredCourses,
                            selectedCourseCodes = uiState.selectedCourseIds.toList(),
                            searchQuery = searchQuery,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = innerPadding.calculateTopPadding() + 24.dp,
                                end = 16.dp,
                                bottom = innerPadding.calculateBottomPadding() + 16.dp
                            ),
                            onSearchQueryChange = { searchQuery = it },
                            onCourseToggle = { course ->
                                if (course.isLocked) return@EnrollmentCourseStepContent
                                viewModel.toggleCourse(course.id)
                            }
                        )
                    }


                    EnrollmentStep.PersonalInfo -> {
                        EnrollmentPersonalInfoStepContent(
                            fullName = fullName,
                            studentId = studentId,
                            emailAddress = emailAddress,
                            phoneNumber = phoneNumber,
                            emergencyContactName = emergencyContactName,
                            relationship = relationship,
                            emergencyPhone = emergencyPhone,
                            contentPadding = PaddingValues(
                                start = 24.dp,
                                top = innerPadding.calculateTopPadding() + 24.dp,
                                end = 24.dp,
                                bottom = innerPadding.calculateBottomPadding() + 32.dp
                            ),
                            onFullNameChange = { fullName = it },
                            onStudentIdChange = { studentId = it },
                            onEmailAddressChange = { emailAddress = it },
                            onPhoneNumberChange = { phoneNumber = it },
                            onEmergencyContactNameChange = { emergencyContactName = it },
                            onRelationshipChange = { relationship = it },
                            onEmergencyPhoneChange = { emergencyPhone = it },
                            onNextClick = { currentStep = EnrollmentStep.Payment }
                        )
                    }

                    EnrollmentStep.Payment -> {
                        EnrollmentPaymentStepContent(
                            selectedCourses = selectedCourses,
                            selectedCredits = selectedCredits,
                            estimatedTuition = estimatedTuition,
                            fullName = fullName,
                            studentId = studentId,
                            emailAddress = emailAddress,
                            phoneNumber = phoneNumber,
                            emergencyContactName = emergencyContactName,
                            relationship = relationship,
                            emergencyPhone = emergencyPhone,
                            contentPadding = PaddingValues(
                                start = 24.dp,
                                top = innerPadding.calculateTopPadding() + 24.dp,
                                end = 24.dp,
                                bottom = innerPadding.calculateBottomPadding() + 32.dp
                            ),
                            onConfirmClick = { 
                                viewModel.submitEnrollment(studentId)
                            }
                        )
                    }

                    EnrollmentStep.Confirmation -> {
                        EnrollmentConfirmationStepContent(
                            courses = confirmationCourses,
                            studentName = fullName.ifBlank { uiState.studentProfile?.fullName ?: "N/A" },
                            studentId = studentId.ifBlank { uiState.studentProfile?.accountId ?: "N/A" },
                            program = uiState.studentProfile?.programSummary?.split("•")?.get(0)?.trim() ?: "N/A", 
                            semester = "Spring 2024",
                            totalUnits = selectedCredits,
                            estimatedTuition = estimatedTuition,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = 16.dp,
                                end = 16.dp,
                                bottom = innerPadding.calculateBottomPadding() + 32.dp
                            ),
                            onBackClick = { currentStep = EnrollmentStep.Payment },
                            onDownloadReceiptClick = onDownloadReceiptClick,
                            onHomeClick = onHomeClick,
                            onAdjustmentClick = onAdjustmentClick
                        )
                    }
                }
            }

            // Observe success to transition
            LaunchedEffect(uiState.enrollmentSuccess) {
                if (uiState.enrollmentSuccess) {
                    currentStep = EnrollmentStep.Confirmation
                }
            }

            // Error Dialog
            if (uiState.error != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text("Enrollment Error") },
                    text = { Text(uiState.error!!) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

private fun previousEnrollmentStep(step: EnrollmentStep): EnrollmentStep? {
    return when (step) {
        EnrollmentStep.Courses -> null
        EnrollmentStep.PersonalInfo -> EnrollmentStep.Courses
        EnrollmentStep.Payment -> EnrollmentStep.PersonalInfo
        EnrollmentStep.Confirmation -> EnrollmentStep.Payment
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EnrollmentScreenPreview() {
    StudentAppTheme(dynamicColor = false) {
        EnrollmentScreen(
            navigationItems = buildPrimaryBottomNavItems(),
            selectedNavItemId = "academic",
            onBottomNavSelected = {},
            onBackClick = {}
        )
    }
}
