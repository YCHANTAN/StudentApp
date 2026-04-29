package com.example.studentapp.ui.screens.changeschedule.components

import androidx.compose.runtime.Composable
import com.example.studentapp.ui.components.StudentPrimaryButton

@Composable
fun ConfirmScheduleButton(
    onClick: () -> Unit
) {
    StudentPrimaryButton(
        text = "Confirm Schedule Change",
        onClick = onClick
    )
}
