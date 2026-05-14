package com.example.studentapp.ui.screens.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.screens.dashboard.models.DashboardStat
import com.example.studentapp.ui.theme.DarkGreen
import com.example.studentapp.ui.theme.Gold

@Composable
fun StatsSection(
    stats: List<DashboardStat>,
    onStatClick: (DashboardStat) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Highlighted stats (like Balance) take full width
        stats.filter { it.isHighlighted }.forEach { stat ->
            StatCard(
                stat = stat,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onStatClick(stat) }
            )
        }

        // Regular stats stay in a row, filling the width to match the lining
        val regularStats = stats.filter { !it.isHighlighted }
        if (regularStats.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                regularStats.forEach { stat ->
                    StatCard(
                        stat = stat,
                        modifier = Modifier.weight(1f),
                        onClick = { onStatClick(stat) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    stat: DashboardStat,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val containerColor = if (stat.isHighlighted) DarkGreen else MaterialTheme.colorScheme.surface
    val borderColor = if (stat.isHighlighted) DarkGreen else MaterialTheme.colorScheme.outlineVariant
    val valueColor = if (stat.isHighlighted) Color.White else MaterialTheme.colorScheme.onSurface
    val labelColor = if (stat.isHighlighted) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (stat.isHighlighted) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = stat.icon,
                contentDescription = stat.label,
                tint = Gold
            )

            Text(
                text = stat.value,
                color = valueColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stat.label,
                color = labelColor,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}
