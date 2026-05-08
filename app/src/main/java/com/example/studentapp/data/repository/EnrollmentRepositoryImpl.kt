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

    override suspend fun getStudyLoad(studentId: String): com.example.studentapp.data.remote.StudyLoadResponse? {
        return try {
            val response = api.getStudyLoad(studentId)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createEnrollment(studentId: String, courseIds: List<String>): EnrollmentResponse? {
        val response = api.createEnrollment(CreateEnrollmentRequest(studentId, courseIds))
        if (response.isSuccessful) {
            val apiResponse = response.body()
            return if (apiResponse?.success == true) {
                apiResponse.data
            } else {
                null
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = parseErrorMessage(errorBody)
            throw Exception(errorMessage ?: "Failed to submit enrollment")
        }
    }

    override suspend fun updateEnrollment(enrollmentId: String, courseIds: List<String>): EnrollmentResponse? {
        val response = api.updateEnrollment(enrollmentId, com.example.studentapp.data.remote.UpdateEnrollmentRequest(courseIds = courseIds))
        if (response.isSuccessful) {
            val apiResponse = response.body()
            return if (apiResponse?.success == true) {
                apiResponse.data
            } else {
                null
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = parseErrorMessage(errorBody)
            throw Exception(errorMessage ?: "Failed to update enrollment")
        }
    }

    private fun parseErrorMessage(json: String?): String? {
        if (json == null) return null
        return try {
            val gson = com.google.gson.Gson()
            val errorObj = gson.fromJson(json, com.google.gson.JsonObject::class.java)
            if (errorObj.has("error")) {
                errorObj.getAsJsonObject("error").get("message").asString
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
