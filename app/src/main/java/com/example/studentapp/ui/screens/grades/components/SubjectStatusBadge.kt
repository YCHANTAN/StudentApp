package com.example.studentapp.ui.screens.grades.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.screens.grades.models.SubjectStatus

@Composable
fun SubjectStatusBadge(status: SubjectStatus) {
    val text = when (status) {
        SubjectStatus.COMPLETED -> "COMPLETED"
        SubjectStatus.IN_PROGRESS -> "IN PROGRESS"
    }

    val bgColor = when (status) {
        SubjectStatus.COMPLETED -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        SubjectStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
    }

    val textColor = when (status) {
        SubjectStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        SubjectStatus.IN_PROGRESS -> MaterialTheme.colorScheme.secondary
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
