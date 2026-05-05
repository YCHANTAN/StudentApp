package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.ComplaintResponse

interface ComplaintRepository {
    suspend fun getComplaints(studentId: String): List<ComplaintResponse>
}
