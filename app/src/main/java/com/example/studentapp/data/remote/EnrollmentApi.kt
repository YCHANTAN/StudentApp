package com.example.studentapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import okhttp3.ResponseBody

data class CreateEnrollmentRequest(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("courseIds") val courseIds: List<String>
)

data class EnrollmentResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("status") val status: String,
    @SerializedName("totalUnits") val totalUnits: Int,
    @SerializedName("totalTuition") val totalTuition: Double,
    @SerializedName("courseIds") val courseIds: List<String>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class UpdateEnrollmentRequest(
    @SerializedName("courseIds") val courseIds: List<String>? = null,
    @SerializedName("status") val status: String? = null
)

data class StudyLoadResponse(
    @SerializedName("courses") val courses: List<CourseResponse>,
    @SerializedName("totalUnits") val totalUnits: Int,
    @SerializedName("semesterLabel") val semesterLabel: String
)

interface EnrollmentApi {
    @GET("enrollments")
    suspend fun getEnrollments(
        @Query("studentId") studentId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<PaginatedResponse<EnrollmentResponse>>

    @GET("enrollments/study-load/student/{studentId}")
    suspend fun getStudyLoad(
        @Path("studentId") studentId: String
    ): Response<ApiResponse<StudyLoadResponse>>

    @POST("enrollments")
    suspend fun createEnrollment(
        @Body request: CreateEnrollmentRequest
    ): Response<ApiResponse<EnrollmentResponse>>

    @retrofit2.http.PATCH("enrollments/{id}")
    suspend fun updateEnrollment(
        @Path("id") id: String,
        @Body request: UpdateEnrollmentRequest
    ): Response<ApiResponse<EnrollmentResponse>>

    @Streaming
    @GET("enrollments/student/{studentId}/pdf")
    suspend fun getStudyLoadPdf(
        @Path("studentId") studentId: String
    ): Response<ResponseBody>
}
