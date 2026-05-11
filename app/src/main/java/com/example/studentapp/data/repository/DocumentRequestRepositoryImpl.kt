package com.example.studentapp.data.repository

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
}
