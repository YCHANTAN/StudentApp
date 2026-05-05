package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.DocumentRequestResponse

interface DocumentRequestRepository {
    suspend fun getDocumentRequests(studentId: String): List<DocumentRequestResponse>
}
