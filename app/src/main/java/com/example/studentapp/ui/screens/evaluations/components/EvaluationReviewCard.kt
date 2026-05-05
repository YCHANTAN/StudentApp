package com.example.studentapp.ui.screens.evaluations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.screens.evaluations.components.EvaluationRatingRow
import com.example.studentapp.ui.screens.evaluations.components.EvaluationSubmitButton

@Composable
fun EvaluationReviewCard(
    courseTitle: String,
    instructor: String,
    teachingQuality: Int,
    courseMaterials: Int,
    punctuality: Int,
    comments: String,
    onTeachingQualityChanged: (Int) -> Unit = {},
    onCourseMaterialsChanged: (Int) -> Unit = {},
    onPunctualityChanged: (Int) -> Unit = {},
    onCommentsChanged: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Column {
                    Text(
                        text = courseTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = instructor,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RateReview,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                EvaluationRatingRow(
                    label = "TEACHING QUALITY",
                    rating = teachingQuality,
                    onRatingChanged = onTeachingQualityChanged
                )

                EvaluationRatingRow(
                    label = "COURSE MATERIALS",
                    rating = courseMaterials,
                    onRatingChanged = onCourseMaterialsChanged
                )

                EvaluationRatingRow(
                    label = "PUNCTUALITY",
                    rating = punctuality,
                    onRatingChanged = onPunctualityChanged
                )

                Column {
                    Text(
                        text = "ADDITIONAL COMMENTS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        BasicTextField(
                            value = comments,
                            onValueChange = onCommentsChanged,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .padding(14.dp),
                            decorationBox = { innerTextField ->
                                if (comments.isEmpty()) {
                                    Text(
                                        text = "Share your experience with the course and instructor...",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                EvaluationSubmitButton(
                    text = "Submit Evaluation",
                    onClick = onSubmitClick
                )
            }
        }
    }
}
