package com.example.studentapp.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.library.models.LibraryTab
import com.example.studentapp.ui.screens.services.components.DocumentRequestsSection
import com.example.studentapp.ui.screens.services.components.DocumentTypeGrid
import com.example.studentapp.ui.screens.services.components.LibraryServicesSection
import com.example.studentapp.ui.screens.services.models.sampleDocumentTypes
import com.example.studentapp.ui.screens.services.models.sampleLibraryLinks

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentNotificationButton
import com.example.studentapp.ui.components.StudentToast
import com.example.studentapp.ui.components.ToastType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    navigationItems: List<StudentBottomNavItem> = buildPrimaryBottomNavItems(),
    selectedNavItemId: String = "services",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onLibraryClick: (LibraryTab) -> Unit = {},
    onTORClick: () -> Unit = {},
    onCOEClick: () -> Unit = {},
    onGoodMoralClick: () -> Unit = {},
    viewModel: ServicesViewModel = viewModel()
) {
    val documentRequests = viewModel.documentRequests
    val isLoading = viewModel.isLoading

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    Scaffold(
        topBar = {
            StudentHeader(
                title = "Services",
                onBackClick = onBackClick,
                actions = {
                    StudentNotificationButton(
                        onClick = onNotificationClick
                    )
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
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading && documentRequests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp)
                ) {
                    // Document Requests
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        DocumentRequestsSection(requests = documentRequests)
                    }

                    // Document Type Quick Actions
                    item {
                        DocumentTypeGrid(
                            documentTypes = sampleDocumentTypes,
                            onDocumentTypeClick = { docType ->
                                when (docType.label) {
                                    "TOR" -> onTORClick()
                                    "Good Moral" -> onGoodMoralClick()
                                    "COE" -> onCOEClick()
                                    else -> {
                                        toastMessage = "Requesting ${docType.label}..."
                                        toastType = ToastType.INFO
                                        showToast = true
                                    }
                                }
                            }
                        )
                    }

                    // Library Services
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        LibraryServicesSection(
                            libraryLinks = sampleLibraryLinks,
                            onBorrowBookClick = { onLibraryClick(LibraryTab.Available) },
                            onReturnClick = { onLibraryClick(LibraryTab.Return) },
                            onLinkClick = { link ->
                                when (link.title) {
                                    "Availability Tracker" -> onLibraryClick(LibraryTab.Available)
                                    "Borrowing History" -> onLibraryClick(LibraryTab.History)
                                }
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }

            StudentToast(
                message = toastMessage,
                type = toastType,
                isVisible = showToast,
                onDismiss = { showToast = false }
            )
        }
    }
}
