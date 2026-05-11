package com.example.studentapp.ui.screens.enrollment.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.ContactMail
import androidx.compose.material.icons.outlined.ContactPhone
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EnrollmentPersonalInfoStepContent(
    fullName: String,
    studentId: String,
    emailAddress: String,
    phoneNumber: String,
    emergencyContactName: String,
    relationship: String,
    emergencyPhone: String,
    contentPadding: PaddingValues,
    onFullNameChange: (String) -> Unit,
    onStudentIdChange: (String) -> Unit,
    onEmailAddressChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onEmergencyContactNameChange: (String) -> Unit,
    onRelationshipChange: (String) -> Unit,
    onEmergencyPhoneChange: (String) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFormValid = fullName.isNotBlank() && 
                      studentId.isNotBlank() && 
                      emailAddress.isNotBlank() && 
                      phoneNumber.isNotBlank() && 
                      emergencyContactName.isNotBlank() && 
                      emergencyPhone.isNotBlank()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            EnrollmentSectionHeader(
                title = "Identification",
                icon = Icons.Outlined.Badge
            )
            EnrollmentTextField(
                label = "Full Legal Name",
                value = fullName,
                onValueChange = onFullNameChange,
                placeholder = "Johnathan Doe"
            )
            EnrollmentTextField(
                label = "Student ID Number",
                value = studentId,
                onValueChange = onStudentIdChange,
                placeholder = "STU-2024-XXXX",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            EnrollmentSectionHeader(
                title = "Contact Details",
                icon = Icons.Outlined.ContactMail
            )
            EnrollmentTextField(
                label = "Email Address",
                value = emailAddress,
                onValueChange = onEmailAddressChange,
                placeholder = "j.doe@example.com"
            )
            EnrollmentTextField(
                label = "Phone Number",
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = "+1 (555) 000-0000",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            EnrollmentSectionHeader(
                title = "Emergency Contact",
                icon = Icons.Outlined.ContactPhone
            )
            EnrollmentTextField(
                label = "Contact Person Name",
                value = emergencyContactName,
                onValueChange = onEmergencyContactNameChange,
                placeholder = "Jane Doe"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EnrollmentSelectField(
                    label = "Relationship",
                    value = relationship,
                    options = listOf("Parent", "Guardian", "Spouse", "Other"),
                    onValueSelected = onRelationshipChange,
                    modifier = Modifier.weight(1f)
                )
                EnrollmentTextField(
                    label = "Emergency Phone",
                    value = emergencyPhone,
                    onValueChange = onEmergencyPhoneChange,
                    placeholder = "+1 (555) 999-9999",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            EnrollmentPrimaryButton(
                text = "Proceed to Payment",
                icon = Icons.Outlined.Payments,
                onClick = onNextClick,
                enabled = isFormValid
            )
            Text(
                text = "By proceeding, you agree to our terms of enrollment.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}
