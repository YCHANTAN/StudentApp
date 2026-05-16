package com.example.studentapp.ui.screens.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.library.models.FilterChip
import com.example.studentapp.ui.screens.library.models.LibraryBook
import com.example.studentapp.ui.screens.library.models.LibraryTab
import com.example.studentapp.ui.screens.library.models.sampleLibraryFilters

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.screens.library.LibraryViewModel

@Composable
fun LibraryContent(
    initialTab: LibraryTab = LibraryTab.Available,
    navigationItems: List<StudentBottomNavItem> = buildPrimaryBottomNavItems(),
    selectedNavItemId: String = "services",
    viewModel: LibraryViewModel = viewModel(),
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onTuneClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTab by rememberSaveable { mutableStateOf(initialTab) }
    var query by rememberSaveable { mutableStateOf("") }
    val booksState = viewModel.booksState
    val isLoading = viewModel.isLoading

    // Update selectedTab if initialTab changes (e.g. on navigation)
    LaunchedEffect(initialTab) {
        selectedTab = initialTab
    }

    val filteredBooks = remember(selectedTab, query, booksState) {
        filterLibraryBooks(
            books = booksState,
            selectedTab = selectedTab,
            query = query
        )
    }

    val filters = remember { sampleLibraryFilters }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LibraryHeaderSection(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onBackClick = onBackClick,
                onNotificationClick = onNotificationClick
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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading && booksState.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        LibrarySearchAndFilters(
                            query = query,
                            filters = filters,
                            onQueryChange = { query = it },
                            onTuneClick = onTuneClick
                        )
                    }

                    item {
                        LibraryBookList(
                            books = filteredBooks,
                            selectedTab = selectedTab,
                            onActionClick = { bookToUpdate ->
                                viewModel.performAction(bookToUpdate) { success, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }

                    item {
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = 12.dp))
                    }
                }
            }
        }
    }
}

private fun filterLibraryBooks(
    books: List<LibraryBook>,
    selectedTab: LibraryTab,
    query: String
): List<LibraryBook> {
    return books
        .filter { book ->
            when (selectedTab) {
                LibraryTab.Available -> book.tab == LibraryTab.Available
                LibraryTab.Return -> book.tab == LibraryTab.Return
                LibraryTab.History -> book.tab == LibraryTab.History
            }
        }
        .filter { book ->
            query.isBlank() ||
                book.title.contains(query, ignoreCase = true) ||
                book.author.contains(query, ignoreCase = true)
        }
}
