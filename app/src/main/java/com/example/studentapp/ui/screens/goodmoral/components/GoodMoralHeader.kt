package com.example.studentapp.ui.screens.goodmoral.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.filled.Notifications
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentNotificationButton

@Composable
fun GoodMoralHeader(
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    StudentHeader(
        title = "Good Moral Certificate",
        onBackClick = onBackClick,
        modifier = modifier,
        actions = {
            StudentNotificationButton(
                onClick = onNotificationClick
            )
        }
    )
}
