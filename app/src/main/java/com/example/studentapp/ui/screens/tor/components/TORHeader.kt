package com.example.studentapp.ui.screens.tor.components

import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Notifications
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentHeaderIconButton
import com.example.studentapp.ui.components.StudentNotificationButton

@Composable
fun TORHeader(
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit = {}
) {
    StudentHeader(
        title = "Request TOR",
        onBackClick = onBackClick,
        actions = {
            StudentNotificationButton(
                onClick = onNotificationClick
            )
        }
    )
}
