package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.EnrollmentResponse
import okhttp3.ResponseBody

interface EnrollmentRepository {
    suspend fun getEnrollments(studentId: String): List<EnrollmentResponse>
    suspend fun createEnrollment(studentId: String, courseIds: List<String>): EnrollmentResponse?
    suspend fun updateEnrollment(enrollmentId: String, courseIds: List<String>): EnrollmentResponse?
    suspend fun getStudyLoadPdf(studentId: String): ResponseBody?
}
