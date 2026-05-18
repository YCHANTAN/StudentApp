package com.example.studentapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.theme.Spacing

@Immutable
data class StudentBottomNavItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)

fun buildPrimaryBottomNavItems(): List<StudentBottomNavItem> {
    return listOf(
        StudentBottomNavItem(
            id = "home",
            label = "Home",
            icon = Icons.Filled.Home
        ),
        StudentBottomNavItem(
            id = "academic",
            label = "Academic",
            icon = Icons.Filled.School
        ),
        StudentBottomNavItem(
            id = "finance",
            label = "Finance",
            icon = Icons.Filled.AccountBalanceWallet
        ),
        StudentBottomNavItem(
            id = "services",
            label = "Services",
            icon = Icons.Filled.Widgets
        ),
        StudentBottomNavItem(
            id = "profile",
            label = "Profile",
            icon = Icons.Filled.Person
        )
    )
}

@Composable
fun StudentBottomNavBar(
    items: List<StudentBottomNavItem>,
    selectedItemId: String,
    onItemSelected: (StudentBottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = Spacing.Large, vertical = Spacing.Small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    BottomNavItemButton(
                        item = item,
                        isSelected = item.id == selectedItemId,
                        onClick = { onItemSelected(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItemButton(
    item: StudentBottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val itemColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val itemWeight = if (isSelected) {
        FontWeight.Bold
    } else {
        FontWeight.Medium
    }

    Column(
        modifier = Modifier
            .clickable(
                enabled = !isSelected,
                onClick = onClick
            )
            .padding(vertical = Spacing.ExtraSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = itemColor
        )

        Text(
            text = item.label,
            color = itemColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = itemWeight
        )
    }
}
