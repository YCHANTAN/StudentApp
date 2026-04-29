package com.example.studentapp.ui.screens.coe.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.components.StudentHeaderIconButton

@Composable
fun COEHeader(
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    StudentHeader(
        title = "Request COE",
        onBackClick = onBackClick,
        actions = {
            StudentHeaderIconButton(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                onClick = onInfoClick
            )
        }
    )
}
