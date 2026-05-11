package com.example.studentapp.ui.screens.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.screens.dashboard.models.ServiceRequestStatus
import com.example.studentapp.ui.theme.DarkGreen
import com.example.studentapp.ui.theme.ErrorRed
import com.example.studentapp.ui.theme.ErrorRedSoft
import com.example.studentapp.ui.theme.Gold
import com.example.studentapp.ui.theme.SuccessGreen
import com.example.studentapp.ui.theme.SuccessGreenSoft
import com.example.studentapp.ui.theme.WarningYellow
import com.example.studentapp.ui.theme.WarningYellowSoft

@Composable
fun RequestStatusSection(requestStatus: ServiceRequestStatus) {
    val (statusColor, statusBg) = when (requestStatus.statusLabel.lowercase()) {
        "active", "finished", "completed", "done" -> SuccessGreen to SuccessGreenSoft
        "processing", "under maintenance", "pending", "ongoing" -> WarningYellow to WarningYellowSoft
        "not processed", "not active", "inactive", "cancelled", "failed" -> ErrorRed to ErrorRedSoft
        else -> WarningYellow to WarningYellowSoft
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Request Status Overview",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = requestStatus.title,
                                tint = statusColor
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = requestStatus.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Text(
                                text = "Reference: ${requestStatus.reference}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = statusBg
                    ) {
                        Text(
                            text = requestStatus.statusLabel,
                            color = statusColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { requestStatus.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = statusColor,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )

                Text(
                    text = "Estimated Completion: ${requestStatus.estimatedCompletion}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
