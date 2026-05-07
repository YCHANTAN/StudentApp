package com.example.studentapp.ui.screens.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.data.remote.PaymentSlipResponse
import com.example.studentapp.ui.components.StudentHeader
import com.example.studentapp.ui.theme.Spacing

@Composable
fun PaymentSlipScreen(
    paymentSlip: PaymentSlipResponse?,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            StudentHeader(
                title = "Payment Slip",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (paymentSlip == null) {
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
                    StudentInfoCard(paymentSlip.toAssessment())
                    Spacer(modifier = Modifier.height(Spacing.Large))
                }

                item {
                    Text("Payment Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(Spacing.Small))
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(Spacing.Medium)) {
                            FeeSummaryRow("Total Assessment", "₱${"%,.2f".format(paymentSlip.totalAssessment)}")
                            FeeSummaryRow("Total Amount Paid", "₱${"%,.2f".format(paymentSlip.totalPaid)}")
                            Divider(modifier = Modifier.padding(vertical = Spacing.Small))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Remaining Balance", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(
                                    "₱${"%,.2f".format(paymentSlip.remainingBalance)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (paymentSlip.remainingBalance > 0) MaterialTheme.colorScheme.error else Color(0xFF047857)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(Spacing.Large))
                }

                if (paymentSlip.officialReceiptNumber != null) {
                    item {
                        Text("Official Receipt", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(Spacing.Small))
                        InfoRow("OR Number", paymentSlip.officialReceiptNumber)
                        InfoRow("Payment Date", paymentSlip.paymentDate?.split("T")?.firstOrNull() ?: "N/A")
                        Spacer(modifier = Modifier.height(Spacing.Large))
                    }
                }

                item {
                    Text("Breakdown", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(Spacing.Small))
                }

                items(paymentSlip.subjects) { subject ->
                    SubjectFeeItem(subject.code, subject.title, subject.units, subject.tuition)
                }

                items(paymentSlip.miscellaneousFees) { fee ->
                    FeeSummaryRow(fee.description, "₱${"%,.2f".format(fee.amount)}")
                }

                item {
                    Spacer(modifier = Modifier.height(Spacing.Huge))
                }
            }
        }
    }
}

// Helper to reuse StudentInfoCard
fun PaymentSlipResponse.toAssessment(): com.example.studentapp.data.remote.AssessmentResponse {
    return com.example.studentapp.data.remote.AssessmentResponse(
        studentName = this.studentName,
        studentId = this.studentId,
        program = this.program,
        semester = this.semester,
        schoolYear = this.schoolYear,
        subjects = this.subjects,
        totalUnits = this.totalUnits,
        totalTuition = this.totalTuition,
        miscellaneousFees = this.miscellaneousFees,
        totalAssessment = this.totalAssessment
    )
}
