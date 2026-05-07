package com.example.studentapp.ui.screens.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.data.remote.AssessmentResponse
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.theme.Spacing

@Composable
fun AssessmentScreen(
    assessment: AssessmentResponse?,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            StudentHeader(
                title = "Assessment",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (assessment == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = Spacing.Medium)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Spacing.Medium))
                    StudentInfoCard(assessment)
                    Spacer(modifier = Modifier.height(Spacing.Large))
                }

                item {
                    Text("Enrolled Subjects", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(Spacing.Small))
                }

                items(assessment.subjects) { subject ->
                    SubjectFeeItem(subject.code, subject.title, subject.units, subject.tuition)
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = Spacing.Medium))
                    FeeSummaryRow("Total Units", "${assessment.totalUnits}")
                    FeeSummaryRow("Total Tuition", "₱${"%,.2f".format(assessment.totalTuition)}")
                    Spacer(modifier = Modifier.height(Spacing.Medium))
                    Text("Miscellaneous Fees", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(Spacing.Small))
                }

                items(assessment.miscellaneousFees) { fee ->
                    FeeSummaryRow(fee.description, "₱${"%,.2f".format(fee.amount)}")
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = Spacing.Medium))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Assessment", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "₱${"%,.2f".format(assessment.totalAssessment)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(Spacing.Huge))
                }
            }
        }
    }
}

@Composable
fun StudentInfoCard(assessment: AssessmentResponse) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Spacing.Medium)) {
            InfoRow("Name", assessment.studentName)
            InfoRow("Student ID", assessment.studentId)
            InfoRow("Program", assessment.program)
            InfoRow("Semester", "${assessment.semester} ${assessment.schoolYear}")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun SubjectFeeItem(code: String, title: String, units: Int, amount: Double) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("$code - $title", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text("₱${"%,.2f".format(amount)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Text("$units Units", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable
fun FeeSummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
