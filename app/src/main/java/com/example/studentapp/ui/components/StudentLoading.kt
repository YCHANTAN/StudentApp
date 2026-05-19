package com.example.studentapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.theme.Radius
import com.example.studentapp.ui.theme.Spacing

@Composable
fun StudentLoadingOverlay(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.3f)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun StudentLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    StudentSkeletonList(modifier = modifier)
}

@Composable
fun StudentSkeletonList(
    modifier: Modifier = Modifier,
    itemCount: Int = 5,
    includeHeader: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        if (includeHeader) {
            StudentSkeletonBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp),
                radius = Radius.ExtraLarge
            )
        }

        repeat(itemCount) {
            StudentSkeletonCard()
        }
    }
}

@Composable
fun StudentSkeletonCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(Radius.Large)
            )
            .padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        StudentSkeletonBlock(
            modifier = Modifier
                .fillMaxWidth(0.62f)
                .height(18.dp)
        )
        StudentSkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        StudentSkeletonBlock(
            modifier = Modifier
                .fillMaxWidth(0.78f)
                .height(12.dp)
        )
    }
}

@Composable
fun StudentSkeletonBlock(
    modifier: Modifier = Modifier,
    radius: androidx.compose.ui.unit.Dp = Radius.Small
) {
    val transition = rememberInfiniteTransition(label = "student-skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "student-skeleton-alpha"
    )
    val color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha)

    Box(
        modifier = modifier.background(
            color = color,
            shape = RoundedCornerShape(radius)
        )
    )
}

@Composable
fun StudentSkeletonScaffold(
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.foundation.layout.PaddingValues,
    itemCount: Int = 5,
    includeHeader: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        StudentSkeletonList(
            itemCount = itemCount,
            includeHeader = includeHeader
        )
    }
}

@Composable
fun StudentHeaderSkeleton(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.statusBarsPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Medium, vertical = Spacing.Medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StudentSkeletonBlock(
                        modifier = Modifier.size(48.dp),
                        radius = 24.dp
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
                        StudentSkeletonBlock(
                            modifier = Modifier
                                .fillMaxWidth(0.28f)
                                .height(12.dp)
                        )
                        StudentSkeletonBlock(
                            modifier = Modifier
                                .fillMaxWidth(0.38f)
                                .height(18.dp)
                        )
                    }
                }

                StudentSkeletonBlock(
                    modifier = Modifier.size(48.dp),
                    radius = 24.dp
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}
