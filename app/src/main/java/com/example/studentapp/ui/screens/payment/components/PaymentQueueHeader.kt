package com.example.studentapp.ui.screens.payment.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun PaymentQueueHeader(
    onBackClick: () -> Unit
) {
    StudentHeader(
        title = "Payment Queue",
        onBackClick = onBackClick
    )
}
