package com.example.studentapp.ui.screens.goodmoral.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.ui.theme.DarkGreen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRequestSection(
    studentId: String,
    fullName: String,
    isSubmitting: Boolean = false,
    onSubmitClick: (program: String, yearLevel: String, purpose: String) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var selectedProgram by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }

    var programExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }

    val programs = listOf("BS Computer Science", "BS Information Technology", "BS Engineering")
    val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = "New Request",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Student Info
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Requesting for: $fullName",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "ID: $studentId",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Program and Year Level Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Select Program
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Select Program",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ExposedDropdownMenuBox(
                    expanded = programExpanded,
                    onExpandedChange = { programExpanded = !programExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedProgram,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select course", fontSize = 14.sp) },
                        trailingIcon = { Icon(Icons.Default.ExpandMore, null) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = programExpanded,
                        onDismissRequest = { programExpanded = false }
                    ) {
                        programs.forEach { program ->
                            DropdownMenuItem(
                                text = { Text(program) },
                                onClick = {
                                    selectedProgram = program
                                    programExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Year Level
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Year Level",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = { yearExpanded = !yearExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedYear,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select level", fontSize = 14.sp) },
                        trailingIcon = { Icon(Icons.Default.ExpandMore, null) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year) },
                                onClick = {
                                    selectedYear = year
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Request Purpose
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Request Purpose",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = purpose,
                onValueChange = { purpose = it },
                placeholder = {
                    Text(
                        "e.g. Scholarship application, Internship, Job requirement...",
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
        }

        // Simple Payment Note for Good Moral
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Request Fee", fontSize = 14.sp)
                Text("₱100.00", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
            Text(
                text = "This fee will be added to your account balance.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Button(
            onClick = { onSubmitClick(selectedProgram, selectedYear, purpose) },
            enabled = !isSubmitting && selectedProgram.isNotEmpty() && selectedYear.isNotEmpty() && purpose.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Text(
                        text = "Request Certificate",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
