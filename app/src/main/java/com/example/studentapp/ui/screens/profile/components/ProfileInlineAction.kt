package com.example.studentapp.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.studentapp.ui.theme.Radius
import com.example.studentapp.ui.theme.Spacing

@Composable
fun ProfileInlineAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.ExtraLarge))
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.Small, vertical = Spacing.ExtraSmall)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}
