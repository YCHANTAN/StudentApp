package com.example.studentapp.ui.screens.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.studentapp.ui.components.StudentBottomNavBar
import com.example.studentapp.ui.components.StudentBottomNavItem
import com.example.studentapp.ui.components.buildPrimaryBottomNavItems
import com.example.studentapp.ui.screens.finance.components.BalanceCard
import com.example.studentapp.ui.screens.finance.components.QuickActionsSection
import com.example.studentapp.ui.screens.finance.components.TransactionHistoryHeader
import com.example.studentapp.ui.screens.finance.components.TransactionItem
import com.example.studentapp.ui.theme.Spacing

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentNotificationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    navigationItems: List<StudentBottomNavItem> = buildPrimaryBottomNavItems(),
    selectedNavItemId: String = "finance",
    onBottomNavSelected: (StudentBottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onPayNowClick: () -> Unit = {},
    onAssessmentClick: () -> Unit = {},
    onPaymentSlipClick: () -> Unit = {},
    viewModel: FinanceViewModel = viewModel()
) {
    val balance = viewModel.balance
    val transactions = viewModel.transactions
    val isLoading = viewModel.isLoading
    Scaffold(
        topBar = {
            StudentHeader(
                title = "Finance",
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
        if (isLoading && transactions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = Spacing.Medium)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Spacing.Small))
                    BalanceCard(
                        balance = balance,
                        onPayNowClick = onPayNowClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(Spacing.Large))
                    QuickActionsSection(
                        onAssessmentClick = onAssessmentClick,
                        onPaymentSlipClick = onPaymentSlipClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(Spacing.Large))
                    TransactionHistoryHeader()
                }

                items(transactions) { transaction ->
                    TransactionItem(transaction)
                    Spacer(modifier = Modifier.height(Spacing.Medium))
                }

                item { Spacer(modifier = Modifier.height(Spacing.Medium)) }
            }
        }
    }
}
