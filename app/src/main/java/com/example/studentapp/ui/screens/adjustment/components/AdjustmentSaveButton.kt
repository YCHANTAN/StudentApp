package com.example.studentapp.ui.screens.adjustment.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Composable
import com.example.studentapp.ui.components.StudentPrimaryButton

@Composable
fun AdjustmentSaveButton(
    text: String,
    onClick: () -> Unit
) {
    StudentPrimaryButton(
        text = text,
        onClick = onClick,
        icon = Icons.Outlined.Save
    )
}
