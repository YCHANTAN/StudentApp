package com.example.studentapp.ui.screens.courses.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.screens.courses.models.CourseEntry
import com.example.studentapp.ui.screens.courses.models.CourseStatus
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CourseCard(
    course: CourseEntry,
    modifier: Modifier = Modifier
) {
    val cardBackground = MaterialTheme.colorScheme.surface

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = if (course.status == CourseStatus.Enrolled) {
            RoundedCornerShape(8.dp)
        } else {
            RoundedCornerShape(16.dp)
        },
        color = cardBackground,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(
                if (course.status == CourseStatus.Enrolled) {
                    16.dp
                } else {
                    20.dp
                }
            ),
            verticalArrangement = Arrangement.spacedBy(
                if (course.status == CourseStatus.Enrolled) {
                    12.dp
                } else {
                    8.dp
                }
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = course.code,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = course.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = if (course.status == CourseStatus.Enrolled) 16.sp else 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                CourseTrailingBadge(course = course)
            }

            CourseMetadataSection(course = course)

            CourseProgressBar(
                progress = course.progress,
                color = if (course.status == CourseStatus.Completed) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                }
            )
        }
    }
}

@Composable
fun CourseTrailingBadge(
    course: CourseEntry,
    modifier: Modifier = Modifier
) {
    if (course.status == CourseStatus.Completed) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Completed",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
        return
    }

    val badgeText = course.units ?: ""
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = badgeText,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CourseMetadataSection(
    course: CourseEntry,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (course.status == CourseStatus.Enrolled) 4.dp else 8.dp
        )
    ) {
        CourseMetadataRow(
            icon = Icons.Outlined.Person,
            text = course.instructor,
            iconTint = if (course.status == CourseStatus.Enrolled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        when (course.status) {
            CourseStatus.Enrolled -> {
                CourseMetadataRow(
                    icon = Icons.Outlined.CalendarToday,
                    text = course.schedule.orEmpty(),
                    iconTint = MaterialTheme.colorScheme.primary
                )
                CourseMetadataRow(
                    icon = Icons.Outlined.LocationOn,
                    text = course.location.orEmpty(),
                    iconTint = MaterialTheme.colorScheme.primary
                )
            }

            CourseStatus.Completed -> {
                CourseMetadataRow(
                    icon = Icons.Outlined.CheckCircle,
                    text = course.grade.orEmpty(),
                    iconTint = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }

            CourseStatus.Waitlisted -> {
                CourseMetadataRow(
                    icon = Icons.Outlined.CalendarToday,
                    text = course.schedule.orEmpty(),
                    iconTint = MaterialTheme.colorScheme.primary
                )
                CourseMetadataRow(
                    icon = Icons.Outlined.LocationOn,
                    text = course.waitlistStatus.orEmpty(),
                    iconTint = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CourseMetadataRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    iconTint: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.padding(top = 1.dp)
        )
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = fontWeight
        )
    }
}

@Composable
fun CourseProgressBar(
    progress: Float,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .clip(CircleShape)
                .background(color)
                .padding(vertical = 3.dp)
        )
    }
}
