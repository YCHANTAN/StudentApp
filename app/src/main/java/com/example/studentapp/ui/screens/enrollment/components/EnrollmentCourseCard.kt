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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.screens.enrollment.models.EnrollableCourse

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember

@Composable
fun EnrollmentCourseCard(
    course: EnrollableCourse,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isInteractive = !course.isLocked && (course.remainingSlots > 0 || isSelected)
    val selectedGreen = Color(0xFF2E7D32)
    val unitYellow = Color(0xFFFBC02D) // A standard material yellow/amber
    
    val interactionSource = remember { MutableInteractionSource() }
    
    val borderColor = when {
        course.isLocked -> MaterialTheme.colorScheme.surfaceVariant
        isSelected -> selectedGreen
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val backgroundColor = when {
        course.isLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        isSelected -> selectedGreen.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.surface
    }
    val alpha = if (course.isLocked || (course.remainingSlots == 0 && !isSelected)) 0.60f else 1f
    val cardShape = RoundedCornerShape(12.dp)
    
    val elevation = animateFloatAsState(
        targetValue = if (isSelected) 6f else 2f,
        animationSpec = tween(durationMillis = 150),
        label = "enrollment_card_elevation"
    )

    val animatedBorderColor = animateColorAsState(
        targetValue = borderColor,
        animationSpec = tween(durationMillis = 150),
        label = "enrollment_card_border"
    )
    val animatedBackgroundColor = animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 150),
        label = "enrollment_card_background"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevation.value.dp,
                shape = cardShape,
                clip = false
            )
            .background(animatedBackgroundColor.value, cardShape)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = animatedBorderColor.value,
                shape = cardShape
            )
            .clip(cardShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Removes the gray ripple effect
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
                                if (isSelected) selectedGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = course.code,
                        color = if (course.isLocked) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            if (isSelected) selectedGreen else MaterialTheme.colorScheme.primary
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

                if (course.isLocked) {
                    Text(
                        text = course.lockReason ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "${course.instructor} • ${course.schedule}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (course.remainingSlots > 0) "Remaining Slots: ${course.remainingSlots}" else "COURSE FULL",
                            color = if (course.remainingSlots > 0) {
                                if (isSelected) selectedGreen else MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            fontSize = 11.sp,
                            fontWeight = if (course.remainingSlots > 0) FontWeight.Medium else FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${course.units} Units",
                    color = if (isSelected) selectedGreen else unitYellow,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                EnrollmentCourseStatusIcon(
                    isSelected = isSelected,
                    isLocked = course.isLocked || (course.remainingSlots == 0 && !isSelected),
                    modifier = Modifier.alpha(if (course.isLocked || (course.remainingSlots == 0 && !isSelected)) 0.6f else 1f)
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
    val selectedGreen = Color(0xFF2E7D32)
    val icon = when {
        isLocked && !isSelected -> Icons.Outlined.Lock
        isSelected -> Icons.Default.Check
        else -> Icons.Outlined.AddCircle
    }
    val tint = when {
        isSelected -> selectedGreen
        isLocked -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.outline
    }

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(24.dp)
    )
}
