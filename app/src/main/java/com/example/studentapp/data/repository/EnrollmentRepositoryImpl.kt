package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.CreateEnrollmentRequest
import com.example.studentapp.data.remote.EnrollmentApi
import com.example.studentapp.data.remote.EnrollmentResponse
import com.example.studentapp.data.remote.NetworkModule
import com.example.studentapp.domain.repository.EnrollmentRepository
import okhttp3.ResponseBody

class EnrollmentRepositoryImpl(
    private val api: EnrollmentApi = NetworkModule.enrollmentApi
) : EnrollmentRepository {
    override suspend fun getEnrollments(studentId: String): List<EnrollmentResponse> {
        return try {
            val response = api.getEnrollments(studentId)
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun createEnrollment(studentId: String, courseIds: List<String>): EnrollmentResponse? {
        return try {
            val response = api.createEnrollment(CreateEnrollmentRequest(studentId, courseIds))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateEnrollment(enrollmentId: String, courseIds: List<String>): EnrollmentResponse? {
        return try {
            val response = api.updateEnrollment(enrollmentId, com.example.studentapp.data.remote.UpdateEnrollmentRequest(courseIds = courseIds))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getStudyLoadPdf(studentId: String): ResponseBody? {
        return try {
            val response = api.getStudyLoadPdf(studentId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
