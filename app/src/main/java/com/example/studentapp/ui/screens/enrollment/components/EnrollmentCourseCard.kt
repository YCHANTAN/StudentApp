package com.example.studentapp.ui.screens.enrollment.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.screens.enrollment.models.EnrollableCourse

@Composable
fun EnrollmentCourseCard(
    course: EnrollableCourse,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isInteractive = !course.isLocked
    val borderColor = when {
        course.isLocked -> MaterialTheme.colorScheme.surfaceVariant
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val backgroundColor = when {
        course.isLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        isSelected -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.surface
    }
    val alpha = if (course.isLocked) 0.60f else 1f
    val cardShape = RoundedCornerShape(12.dp)
    val animatedBorderColor = animateColorAsState(
        targetValue = borderColor,
        animationSpec = tween(durationMillis = 220),
        label = "enrollment_card_border"
    )
    val animatedBackgroundColor = animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 220),
        label = "enrollment_card_background"
    )
    val animatedAlpha = animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 220),
        label = "enrollment_card_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = cardShape,
                clip = false
            )
            .clip(cardShape)
            .background(animatedBackgroundColor.value)
            .border(
                width = 2.dp,
                color = animatedBorderColor.value,
                shape = cardShape
            )
            .clickable(
                enabled = isInteractive,
                onClick = onClick
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (course.isLocked) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = course.code,
                        color = if (course.isLocked) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp
                    )
                }

                Text(
                    text = course.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = course.lockReason ?: course.instructorSchedule,
                    color = if (course.isLocked) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${course.units} Units",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                EnrollmentCourseStatusIcon(
                    isSelected = isSelected,
                    isLocked = course.isLocked,
                    modifier = Modifier.alpha(animatedAlpha.value)
                )
            }
        }
    }
}

@Composable
fun EnrollmentCourseStatusIcon(
    isSelected: Boolean,
    isLocked: Boolean,
    modifier: Modifier = Modifier
) {
    val icon = when {
        isLocked -> Icons.Outlined.Lock
        isSelected -> Icons.Filled.CheckCircle
        else -> Icons.Outlined.AddCircle
    }
    val tint = when {
        isLocked -> MaterialTheme.colorScheme.onSurfaceVariant
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(24.dp)
    )
}
