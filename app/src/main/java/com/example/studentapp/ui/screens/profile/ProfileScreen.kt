package com.example.studentapp.ui.screens.profile

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentapp.domain.usecase.GetProfileOverviewUseCase
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.profile.components.ProfileHeader
import com.example.studentapp.ui.screens.profile.components.ProfileSectionDetailContent
import com.example.studentapp.ui.screens.profile.components.ProfileShortcutSection
import com.example.studentapp.ui.screens.profile.components.ProfileSummaryCard
import com.example.studentapp.ui.screens.profile.models.ProfileSectionDestination
import com.example.studentapp.ui.screens.profile.models.ProfileUiState
import com.example.studentapp.ui.screens.profile.models.buildProfileShortcutDestinations
import com.example.studentapp.ui.screens.profile.models.buildProfileAvatarInitials
import com.example.studentapp.ui.screens.profile.models.toUiState
import com.example.studentapp.ui.screens.profile.state.rememberProfileScreenState
import com.example.studentapp.ui.theme.StudentAppTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun ProfileScreen(
    navigationItems: List<StudentBottomNavItem>,
    selectedNavItemId: String,
    onBottomNavSelected: (StudentBottomNavItem) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val state = viewModel.state

    if (state == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val screenState = rememberProfileScreenState(initialState = state)
    val context = LocalContext.current
    val shortcutDestinations = remember {
        buildProfileShortcutDestinations()
    }

    var selectedDestination by rememberSaveable {
        mutableStateOf<ProfileSectionDestination?>(null)
    }

    val currentFullName = screenState.profileForm.fullName
    val currentAvatarInitials = remember(currentFullName) {
        buildProfileAvatarInitials(currentFullName)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ProfileHeader(
                onBackClick = {
                    if (selectedDestination == null) {
                        onBackClick()
                    } else {
                        selectedDestination = null
                    }
                },
                title = selectedDestination?.title ?: "Profile",
                actionLabel = if (selectedDestination == null) {
                    "Support"
                } else {
                    "Overview"
                },
                onActionClick = {
                    selectedDestination = null
                }
            )
        },
        bottomBar = {
            StudentBottomNavBar(
                items = navigationItems,
                selectedItemId = selectedNavItemId,
                onItemSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = if (selectedDestination == null) 1120.dp else 760.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedDestination == null) {
                    ProfileSummaryCard(
                        fullName = currentFullName,
                        accountLabel = state.accountLabel,
                        programSummary = state.programSummary,
                        accountId = state.accountId,
                        avatarInitials = currentAvatarInitials
                    )

                    ProfileShortcutSection(
                        destinations = shortcutDestinations,
                        onDestinationClick = { destination ->
                            selectedDestination = destination
                        }
                    )
                } else {
                    ProfileSectionDetailContent(
                        destination = selectedDestination!!,
                        state = state,
                        screenState = screenState,
                        onDownloadQrCode = { uri: Uri ->
                            screenState.downloadQrCode(
                                context = context,
                                outputUri = uri,
                                payload = state.qrPayload
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    StudentAppTheme(dynamicColor = false) {
        ProfileScreen(
            navigationItems = buildPrimaryBottomNavItems(),
            selectedNavItemId = "profile",
            onBottomNavSelected = {},
            onBackClick = {}
        )
    }
}
