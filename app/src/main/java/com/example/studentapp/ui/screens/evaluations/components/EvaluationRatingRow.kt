package com.example.studentapp.ui.screens.evaluations.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EvaluationRatingRow(
    label: String,
    rating: Int,
    onRatingChanged: (Int) -> Unit = {}
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            repeat(5) { index ->
                val starNumber = index + 1
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star $starNumber",
                    tint = if (starNumber <= rating) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onRatingChanged(starNumber) }
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}
