package com.example.studentapp.ui.screens.programs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.programs.components.ProgramCard
import com.example.studentapp.ui.screens.programs.components.ProgramsHeaderSection
import com.example.studentapp.ui.screens.programs.models.ProgramEntry
import com.example.studentapp.ui.screens.programs.models.ProgramsTab
import com.example.studentapp.ui.screens.programs.models.buildProgramEntries
import com.example.studentapp.ui.screens.programs.models.filterProgramEntries
import com.example.studentapp.ui.theme.StudentAppTheme

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.screens.programs.components.ProspectusViewer

import android.widget.Toast
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun ProgramsScreen(
    navigationItems: List<StudentBottomNavItem>,
    selectedNavItemId: String,
    onBottomNavSelected: (StudentBottomNavItem) -> Unit,
    onBackClick: () -> Unit,
    onDownloadProspectusClick: (ProgramEntry) -> Unit = {},
    onViewProgramClick: (ProgramEntry) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: ProgramsViewModel = viewModel()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(ProgramsTab.AllPrograms) }
    var selectedProspectusUrl by remember { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current

    val programs = viewModel.allPrograms
    val isLoading = viewModel.isLoading
    
    val filteredPrograms = remember(searchQuery, selectedTab, programs) {
        filterProgramEntries(
            programs = programs,
            searchQuery = searchQuery,
            selectedTab = selectedTab
        )
    }

    if (selectedProspectusUrl != null) {
        Dialog(
            onDismissRequest = { selectedProspectusUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            ProspectusViewer(
                url = selectedProspectusUrl!!,
                onClose = { selectedProspectusUrl = null }
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        val useCenteredShell = maxWidth > 420.dp
        val shellModifier = if (useCenteredShell) {
            Modifier
                .padding(vertical = 12.dp)
                .fillMaxHeight()
                .fillMaxWidth(0.92f)
        } else {
            Modifier.fillMaxSize()
        }

        Surface(
            modifier = shellModifier,
            color = MaterialTheme.colorScheme.background,
            shadowElevation = if (useCenteredShell) 18.dp else 0.dp
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    ProgramsHeaderSection(
                        searchQuery = searchQuery,
                        selectedTab = selectedTab,
                        onBackClick = onBackClick,
                        onSearchQueryChange = { searchQuery = it },
                        onTabSelected = { selectedTab = it }
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
                if (isLoading && programs.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    ProgramsContent(
                        programs = filteredPrograms,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = innerPadding.calculateTopPadding() + 16.dp,
                            end = 16.dp,
                            bottom = innerPadding.calculateBottomPadding() + 16.dp
                        ),
                        onDownloadProspectusClick = { entry ->
                            if (entry.prospectusUrl != null) {
                                Toast.makeText(context, "Downloading prospectus...", Toast.LENGTH_SHORT).show()
                                viewModel.downloadProspectus(context, entry) { file ->
                                    if (file != null) {
                                        Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show()
                                        onDownloadProspectusClick(entry)
                                    } else {
                                        Toast.makeText(context, "Failed to download prospectus", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Prospectus not available", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onViewProgramClick = { entry ->
                            if (entry.prospectusUrl != null) {
                                selectedProspectusUrl = entry.prospectusUrl
                            } else {
                                Toast.makeText(context, "Prospectus not available", Toast.LENGTH_SHORT).show()
                            }
                            onViewProgramClick(entry)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProgramsContent(
    programs: List<ProgramEntry>,
    contentPadding: PaddingValues,
    onDownloadProspectusClick: (ProgramEntry) -> Unit,
    onViewProgramClick: (ProgramEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        items(
            items = programs,
            key = { it.id }
        ) { entry ->
            ProgramCard(
                entry = entry,
                onDownloadProspectusClick = { onDownloadProspectusClick(entry) },
                onViewProgramClick = { onViewProgramClick(entry) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProgramsScreenPreview() {
    StudentAppTheme(dynamicColor = false) {
        ProgramsScreen(
            navigationItems = buildPrimaryBottomNavItems(),
            selectedNavItemId = "academic",
            onBottomNavSelected = {},
            onBackClick = {},
            onDownloadProspectusClick = {},
            onViewProgramClick = {}
        )
    }
}
