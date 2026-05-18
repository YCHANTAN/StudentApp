package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.DocumentRequestResponse

interface DocumentRequestRepository {
    suspend fun getDocumentRequests(studentId: String): List<DocumentRequestResponse>
    suspend fun createDocumentRequest(
        studentId: String,
        type: String,
        purpose: String,
        program: String? = null,
        yearLevel: String? = null,
        copies: Int? = null,
        deliveryMethod: String? = null
    ): Boolean
}
