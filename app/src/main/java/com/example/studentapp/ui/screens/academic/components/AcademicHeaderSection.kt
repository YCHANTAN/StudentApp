package com.example.studentapp.ui.screens.academic.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentNotificationButton
import com.example.studentapp.ui.theme.Spacing

@Composable
fun AcademicHeaderSection(
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    StudentHeader(
        title = "Academic Services",
        onBackClick = onBackClick,
        modifier = modifier,
        actions = {
            StudentNotificationButton(
                onClick = onNotificationClick
            )
        }
    )
}
