package com.example.studentapp.ui.screens.coe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.theme.DarkGreen

@Composable
fun COEQuickActions(
    onGenerateDigitalClick: () -> Unit = {},
    onRequestPrintedClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Digital Option (Primary)
        QuickActionCard(
            title = "Generate Digital COE",
            subtitle = "Download a secured PDF version immediately.",
            actionText = "Process Now",
            actionIcon = Icons.Default.ArrowForward,
            icon = Icons.Default.PictureAsPdf,
            badge = "INSTANT",
            isPrimary = true,
            onClick = onGenerateDigitalClick
        )

        // Printed Option (Secondary)
        QuickActionCard(
            title = "Request Printed Copy",
            subtitle = "Pick up at the Registrar's Office for only ₱100.00.",
            actionText = "Submit Request",
            actionIcon = Icons.Default.Schedule,
            icon = Icons.Default.Print,
            isPrimary = false,
            onClick = onRequestPrintedClick
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    actionText: String,
    actionIcon: ImageVector,
    icon: ImageVector,
    badge: String? = null,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isPrimary) DarkGreen else MaterialTheme.colorScheme.surface
    val contentColor = if (isPrimary) Color.White else MaterialTheme.colorScheme.onSurface
    val borderColor = if (isPrimary) Color.Transparent else MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .then(
                if (!isPrimary) Modifier.border(1.dp, borderColor, RoundedCornerShape(16.dp))
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isPrimary) Color.White.copy(alpha = 0.15f) else DarkGreen.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isPrimary) Color.White else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            if (badge != null) {
                Text(
                    text = badge,
                    color = DarkGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = if (isPrimary) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = actionText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPrimary) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = actionIcon,
                contentDescription = null,
                tint = if (isPrimary) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
