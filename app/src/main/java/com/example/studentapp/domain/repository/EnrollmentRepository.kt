package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.EnrollmentResponse

interface EnrollmentRepository {
    suspend fun getEnrollments(studentId: String): List<EnrollmentResponse>
    suspend fun createEnrollment(studentId: String, courseIds: List<String>): EnrollmentResponse?
    suspend fun updateEnrollment(enrollmentId: String, courseIds: List<String>): EnrollmentResponse?
}
