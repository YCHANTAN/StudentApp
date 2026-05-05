package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.ComplaintResponse
import com.example.studentapp.data.remote.NetworkModule
import com.example.studentapp.domain.repository.ComplaintRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ComplaintRepositoryImpl : ComplaintRepository {
    override suspend fun getComplaints(studentId: String): List<ComplaintResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.complaintApi.getComplaints(studentId)
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
