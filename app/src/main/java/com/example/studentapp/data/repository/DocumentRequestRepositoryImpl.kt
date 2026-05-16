package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.CreateDocumentRequestRequest
import com.example.studentapp.data.remote.DocumentRequestResponse
import com.example.studentapp.data.remote.NetworkModule
import com.example.studentapp.domain.repository.DocumentRequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DocumentRequestRepositoryImpl : DocumentRequestRepository {
    override suspend fun getDocumentRequests(studentId: String): List<DocumentRequestResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.documentRequestApi.getDocumentRequests(studentId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    apiResponse.data
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun createDocumentRequest(
        studentId: String,
        type: String,
        purpose: String,
        program: String?,
        yearLevel: String?,
        copies: Int?,
        deliveryMethod: String?
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = CreateDocumentRequestRequest(
                studentId = studentId,
                type = type,
                purpose = purpose,
                program = program,
                yearLevel = yearLevel,
                copies = copies,
                deliveryMethod = deliveryMethod
            )
            val response = NetworkModule.documentRequestApi.createDocumentRequest(request)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            false
        }
    }
}
